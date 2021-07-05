package com.mediaportal.timesheetapi;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.apache.commons.io.FilenameUtils;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    public static void main(String[] args) {

        Planilha planilha = new Planilha();

        File folder = new File("C:\\Users\\Media Portal\\Desktop\\Planilhas\\");
        File[] files = folder.listFiles();
        List<Valores> teste = new ArrayList();

        for (File file : files) {
            if (FilenameUtils.getExtension(file.getName()).toLowerCase().equals("xls") || FilenameUtils.getExtension(file.getName()).toLowerCase().equals("xlsx")) {
                try {
                    teste.addAll(planilha.carregaValoresDoExcelEmLista(file.getPath()));
                } catch (IOException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                }

            }

        }
        String resultado;
        System.out.println(resultado = Relatorio.geraRelatorio(teste));
        Relatorio.writeToFile(teste);
//        System.out.println(somahoras.somarHoras(linhaPlanilha));
//        System.out.println(somahoras.somarHorasFuncionario(linhaPlanilha, "Giovanny Azevedo"));
//        System.out.println(somahoras.somarHorasProjeto(linhaPlanilha, "Admin"));
//        System.out.println(somahoras.somarHorasFuncionarioProjeto(linhaPlanilha, "Nickolas", "Admin"));
//        System.out.println(planilha.carregaFuncionarios(linhaPlanilha).toString());
//        System.out.println(planilha.carregaProjetos(linhaPlanilha).toString());
//        
    }

}
