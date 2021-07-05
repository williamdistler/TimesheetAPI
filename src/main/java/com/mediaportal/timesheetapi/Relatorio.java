/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mediaportal.timesheetapi;

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

    public static void writeToFile(List<Valores> lista) {
        try {
            FileWriter myWriter = new FileWriter("relatorio" + LocalDate.now() + ".log");
            myWriter.write(geraRelatorio(lista));
            myWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public static String geraRelatorio(List<Valores> lista) {
        Set<String> funcionario = Utils.carregaFuncionarios(lista);
        Set<String> projeto = Utils.carregaProjetos(lista);

        String resultado = "";

        resultado += "-------Funcionários------" + System.lineSeparator();
        resultado = funcionario.stream().map((func) -> func + ": " + Utils.decimalFortmat(Utils.somarHorasFuncionario(lista, func)/60.0) + " horas trabalhadas no total" + System.lineSeparator()).reduce(resultado, String::concat);

        resultado += System.lineSeparator() + "---------Projetos--------" + System.lineSeparator();
        resultado = projeto.stream().map((proj) -> proj + ": " + Utils.decimalFortmat(Utils.somarHorasProjeto(lista, proj)/60.0) + " horas de projeto no total" + System.lineSeparator()).reduce(resultado, String::concat);

        resultado += System.lineSeparator() + "---------Warnings--------" + System.lineSeparator();
        resultado = warnings.stream().map((warn) -> warn + System.lineSeparator()).reduce(resultado, String::concat);

        resultado += System.lineSeparator() + "-----------ERROS----------" + System.lineSeparator();
        resultado = errors.stream().map((erro) -> erro + System.lineSeparator()).reduce(resultado, String::concat);
        
        return resultado;

    }

}
