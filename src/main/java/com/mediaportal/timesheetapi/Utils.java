/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mediaportal.timesheetapi;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalTime;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;

/**
 *
 * @author Media Portal
 */
public class Utils {

    public static String dateFormat(Date date) {
        SimpleDateFormat formatador = new SimpleDateFormat("dd/MM/yyyy");
        return formatador.format(date);
    }

    public static String getHorasHHMM(Double hour) {
        return HSSFDateUtil.getJavaDate(hour).getHours() + ":" + HSSFDateUtil.getJavaDate(hour).getMinutes();
    }

    public static Set<String> carregaProjetos(List<Valores> linhas) {
        Set<String> projetos = new HashSet<String>();
        for (int i = 0; i < linhas.size(); i++) {
            projetos.add(linhas.get(i).getIdProjeto());
        }
        return projetos;
    }

    public static Set<String> carregaFuncionarios(List<Valores> linhas) {
        Set<String> funcionario = new HashSet<String>();
        for (int i = 0; i < linhas.size(); i++) {
            funcionario.add(linhas.get(i).getFuncionario());
        }
        return funcionario;
    }

    public static Integer somarHorasFuncionario(List<Valores> linhaPlanilha, String nomeFuncionario) {
        int somaMinutos = 0;
        for (int i = 0; i < linhaPlanilha.size(); i++) {
            if (linhaPlanilha.get(i).getFuncionario().toUpperCase().equals(nomeFuncionario.toUpperCase())) {
                somaMinutos += stringParaMinutos(linhaPlanilha.get(i).getHorasTrabalhadas());
            }
        }
        return somaMinutos;
    }

    public static Integer somarHorasSemFiltro(List<Valores> linhaPlanilha) {
        int somaMinutos = 0;
        for (int i = 0; i < linhaPlanilha.size(); i++) {
            somaMinutos += stringParaMinutos(linhaPlanilha.get(i).getHorasTrabalhadas());
        }
        return somaMinutos;
    }

    public static Integer somarHorasProjeto(List<Valores> linhaPlanilha, String projeto) {
        int somaMinutos = 0;
        for (int i = 0; i < linhaPlanilha.size(); i++) {
            if (linhaPlanilha.get(i).getIdProjeto().toUpperCase().equals(projeto.toUpperCase())) {
                somaMinutos += stringParaMinutos(linhaPlanilha.get(i).getHorasTrabalhadas());
            }
        }
        return somaMinutos;
    }

    public static Integer somarHorasFuncionarioProjeto(List<Valores> linhaPlanilha, String funcionario, String projeto) {
        int somaMinutos = 0;
        for (int i = 0; i < linhaPlanilha.size(); i++) {
            if (linhaPlanilha.get(i).getProjeto().toUpperCase().equals(projeto.toUpperCase()) && linhaPlanilha.get(i).getFuncionario().toUpperCase().equals(funcionario.toUpperCase())) {
                somaMinutos += stringParaMinutos(linhaPlanilha.get(i).getHorasTrabalhadas());
            }
        }
        return somaMinutos;
    }

    public static String minutosParaHoras(Integer minutos) {
        return LocalTime.MIN.plus(Duration.ofMinutes(minutos)).toString();
    }
    
    public static String decimalFortmat(Double num){
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(num);
    }

    private static int stringParaMinutos(String horas) throws NumberFormatException {
        String[] horasMinutos = horas.split(":");
        int horaMinuto = Integer.parseInt(horasMinutos[0]) * 60;
        int minutos = Integer.parseInt(horasMinutos[1]);
        int soma = horaMinuto + minutos;
        return soma;
    }
    
    

}
