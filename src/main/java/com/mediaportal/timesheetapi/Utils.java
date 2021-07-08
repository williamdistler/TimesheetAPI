/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mediaportal.timesheetapi;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
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

    public static Integer somarHorasFuncionario(List<Valores> linhaPlanilha, String nomeFuncionario, Integer mes, Integer ano) {
        int somaMinutos = 0;
        for (int i = 0; i < linhaPlanilha.size(); i++) {
            if (linhaPlanilha.get(i).getFuncionario().toUpperCase().equals(nomeFuncionario.toUpperCase())) {

                LocalDate date = stringToLocalDate(linhaPlanilha, i);
                if (date.getMonth().getValue() == mes && date.getYear() == ano) {
                    somaMinutos += stringParaMinutos(linhaPlanilha.get(i).getHorasTrabalhadas());
                }
            }
        }
        return somaMinutos;
    }

    public static Integer somarHorasProjeto(List<Valores> linhaPlanilha, String projeto, Integer mes, Integer ano) {
        int somaMinutos = 0;
        for (int i = 0; i < linhaPlanilha.size(); i++) {
            if (linhaPlanilha.get(i).getIdProjeto().toUpperCase().equals(projeto.toUpperCase())) {
                LocalDate date = stringToLocalDate(linhaPlanilha, i);
                if (date.getMonth().getValue() == mes && date.getYear() == ano) {
                    somaMinutos += stringParaMinutos(linhaPlanilha.get(i).getHorasTrabalhadas());
                }
            }
        }
        return somaMinutos;
    }

    public static Integer somarHorasFuncionarioProjeto(List<Valores> linhaPlanilha, String funcionario, String projeto, Integer mes, Integer ano) {
        int somaMinutos = 0;
        for (int i = 0; i < linhaPlanilha.size(); i++) {
            if (linhaPlanilha.get(i).getIdProjeto().toUpperCase().equals(projeto.toUpperCase()) && linhaPlanilha.get(i).getFuncionario().toUpperCase().equals(funcionario.toUpperCase())) {
                LocalDate date = stringToLocalDate(linhaPlanilha, i);
                if (date.getMonth().getValue() == mes && date.getYear() == ano) {
                    somaMinutos += stringParaMinutos(linhaPlanilha.get(i).getHorasTrabalhadas());
                }
            }
        }
        return somaMinutos;
    }

    public static String minutosParaHoras(Integer minutos) {
        return LocalTime.MIN.plus(Duration.ofMinutes(minutos)).toString();
    }

    private static LocalDate stringToLocalDate(List<Valores> linhaPlanilha, int i) throws NumberFormatException {
        Integer day = Integer.parseInt(linhaPlanilha.get(i).getData().substring(0, 2));
        Integer month = Integer.parseInt(linhaPlanilha.get(i).getData().substring(3, 5));
        Integer year = Integer.parseInt(linhaPlanilha.get(i).getData().substring(6, 10));
        LocalDate date = LocalDate.of(year, Month.of(month), day);
        return date;
    }

    public static Integer somarHorasSemFiltro(List<Valores> linhaPlanilha) {
        int somaMinutos = 0;
        for (int i = 0; i < linhaPlanilha.size(); i++) {
            somaMinutos += stringParaMinutos(linhaPlanilha.get(i).getHorasTrabalhadas());
        }
        return somaMinutos;
    }

    public static String decimalComDuasCasas(Double num) {
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

    public static String formatDoubleToDate(Double doubleData) {
        Date data;
        String dataFormatada;
        data = HSSFDateUtil.getJavaDate(doubleData);
        dataFormatada = Utils.dateFormat(data);
        return dataFormatada;
    }

}
