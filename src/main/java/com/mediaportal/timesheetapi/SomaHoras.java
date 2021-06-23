package com.mediaportal.timesheetapi;

import java.util.List;

public class SomaHoras {

    public Integer somarHoras(List<Valores> linhaPlanilha) {
        int somaMinutos = 0;
        for (int i = 0; i < linhaPlanilha.size(); i++) {
            somaMinutos = getHora(linhaPlanilha, i, somaMinutos);
        }
        return somaMinutos;
    }

    public Integer somarHorasFuncionario(List<Valores> linhaPlanilha, String nomeFuncionario) {
        int somaMinutos = 0;
        for (int i = 0; i < linhaPlanilha.size(); i++) {
            if (linhaPlanilha.get(i).getFuncionario().toUpperCase().equals(nomeFuncionario.toUpperCase())) {
                somaMinutos = getHora(linhaPlanilha, i, somaMinutos);
            }
        }
        return somaMinutos;
    }

    public Integer somarHorasProjeto(List<Valores> linhaPlanilha, String projeto) {
        int somaMinutos = 0;
        for (int i = 0; i < linhaPlanilha.size(); i++) {
            if (linhaPlanilha.get(i).getProjeto().toUpperCase().equals(projeto.toUpperCase())) {
                somaMinutos = getHora(linhaPlanilha, i, somaMinutos);
            }
        }
        return somaMinutos;
    }

    public Integer somarHorasFuncionarioProjeto(List<Valores> linhaPlanilha, String funcionario, String projeto) {
        int somaMinutos = 0;
        for (int i = 0; i < linhaPlanilha.size(); i++) {
            if (linhaPlanilha.get(i).getProjeto().toUpperCase().equals(projeto.toUpperCase()) && linhaPlanilha.get(i).getFuncionario().toUpperCase().equals(funcionario.toUpperCase())) {
                somaMinutos = getHora(linhaPlanilha, i, somaMinutos);
            }
        }
        return somaMinutos;
    }
    
    private int getHora(List<Valores> linhaPlanilha, int i, int somaMinutos) throws NumberFormatException {
        String hora = linhaPlanilha.get(i).getHorasTrabalhadas();
        int horaMinuto = (Integer.parseInt(hora.substring(0, 2)) * 60);
        int minutos = Integer.parseInt(hora.substring(3, 5));
        int soma = horaMinuto + minutos;
        somaMinutos += soma;
        return somaMinutos;
    }
    
}
