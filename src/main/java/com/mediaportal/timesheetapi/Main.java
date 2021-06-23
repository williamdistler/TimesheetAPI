package com.mediaportal.timesheetapi;

import java.util.List;
import java.util.Set;

public class Main {

    public static void main(String[] args) {
        
        Planilha planilha = new Planilha();
        SomaHoras somahoras = new SomaHoras();
        List<Valores> linhaPlanilha = planilha.lerPlanilhaOld("C:\\Users\\Media Portal\\Documents\\Relatórios\\filesexcel.log");
//        System.out.println(somahoras.somarHoras(linhaPlanilha));
//        System.out.println(somahoras.somarHorasFuncionario(linhaPlanilha, "Giovanny Azevedo"));
//        System.out.println(somahoras.somarHorasProjeto(linhaPlanilha, "Admin"));
//        System.out.println(somahoras.somarHorasFuncionarioProjeto(linhaPlanilha, "Nickolas", "Admin"));
        
        System.out.println(planilha.carregaFuncionarios(linhaPlanilha).toString());
        System.out.println(planilha.carregaProjetos(linhaPlanilha).toString());
        
        Set<String> funcionario = planilha.carregaFuncionarios(linhaPlanilha);
        Set<String> projeto = planilha.carregaProjetos(linhaPlanilha);
        
        funcionario.forEach(func -> System.out.println(func + ": " + somahoras.somarHorasFuncionario(linhaPlanilha,func)));
        projeto.forEach(proj -> System.out.println(proj + ": " + somahoras.somarHorasProjeto(linhaPlanilha, proj)));
    }
    
}
