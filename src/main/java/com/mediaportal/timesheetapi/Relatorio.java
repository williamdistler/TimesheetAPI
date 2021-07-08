/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mediaportal.timesheetapi;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Media Portal
 */
public class Relatorio {

    public static List<String> errors = new ArrayList();
    public static List<String> warnings = new ArrayList();

    public static void writeToFile(List<Valores> lista, String dataVigente) {
        try {
            FileWriter myWriter = new FileWriter("relatorio" + LocalDate.now() + ".log");
            myWriter.write(geraRelatorio(lista, dataVigente));
            myWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public static String geraRelatorio(List<Valores> lista, String dataVigente) {
        Set<String> funcionario = Utils.carregaFuncionarios(lista);
        Set<String> projeto = Utils.carregaProjetos(lista);
        Integer ano = Integer.parseInt(dataVigente.substring(0, 4));
        Integer mes = Integer.parseInt(dataVigente.substring(4));

        String resultado = "";
        resultado += "-------Funcionários------" + System.lineSeparator();
        resultado = funcionario.stream().map((func) -> func + ": " + Utils.decimalComDuasCasas(Utils.somarHorasFuncionario(lista, func, mes, ano) / 60.0) + " horas trabalhadas no total" + System.lineSeparator()).filter(v -> !v.contains("0,00")).reduce(resultado, String::concat);

        resultado += System.lineSeparator() + System.lineSeparator() + "---------Projetos--------" + System.lineSeparator();
        resultado = projeto.stream().map((proj) -> proj + ": " + Utils.decimalComDuasCasas(Utils.somarHorasProjeto(lista, proj, mes, ano) / 60.0) + " horas de projeto no total" + System.lineSeparator()).filter(v -> !v.contains("0,00")).reduce(resultado, String::concat);

        resultado += System.lineSeparator() + System.lineSeparator() + "---------Projetos/Funcionario--------" + System.lineSeparator();
        
        
        for(String func : funcionario){
            for(String proj : projeto){
                Double horas = Utils.somarHorasFuncionarioProjeto(lista,func, proj, mes, ano) / 60.0;
                if(horas > 0)
                    resultado += func +"/" + proj+  ": " + Utils.decimalComDuasCasas(horas) + System.lineSeparator();
            }
        }
        
        projeto.stream().forEach(proj -> {
            funcionario.stream().forEach( func -> Utils.decimalComDuasCasas(Utils.somarHorasFuncionarioProjeto(lista,func, proj, mes, ano) / 60.0));
        });

        resultado += System.lineSeparator() + System.lineSeparator() + "---------Warnings--------" + System.lineSeparator();
        resultado = warnings.stream().map((warn) -> warn + System.lineSeparator()).filter(v -> v.contains("/" + dataVigente.substring(4) + "/")).reduce(resultado, String::concat);

        resultado += System.lineSeparator() + System.lineSeparator() + "-----------ERROS----------" + System.lineSeparator();
        resultado = errors.stream().map((erro) -> erro + System.lineSeparator()).reduce(resultado, String::concat);

        return resultado;
    }

}
