package com.mediaportal.timesheetapi;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.commons.io.FilenameUtils;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    
    File arquivo = new File("C:\\Users\\Media Portal\\Desktop\\Planilhas\\config.txt");
        
    public static String verificaData(File arquivo) throws FileNotFoundException, IOException {
        BufferedReader br = new BufferedReader(new FileReader(arquivo));
        String data = br.readLine();
        //System.out.println(data);
        return data;
    }

    public static void main(String[] args) throws FileNotFoundException, IOException {
        
        Planilha planilha = new Planilha();

        File folder = new File("C:\\Users\\Media Portal\\Desktop\\Planilhas\\");
        File[] files = folder.listFiles();
        List<Valores> teste = new ArrayList();

        for (File file : files) {
            System.out.println(file.getName());
            if (verificaExtensao(file)) {
                try {
                    teste.addAll(planilha.carregaValoresDoExcelEmLista(file.getPath()));
                } catch (IOException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                }

            }

        }
        
        //Falta conseguir fornecer pra main a data da planilha, para poder gerar o relatório
        Date date = Utils.formatDoubleToDate(Planilha.carregaDados().doubleData);

        System.out.println(Relatorio.geraRelatorio(teste, Main.verificaData(arquivo), Utils.dateFormat(date)));
        Relatorio.writeToFile(teste);
//        System.out.println(somahoras.somarHoras(linhaPlanilha));
//        System.out.println(somahoras.somarHorasFuncionario(linhaPlanilha, "Giovanny Azevedo"));
//        System.out.println(somahoras.somarHorasProjeto(linhaPlanilha, "Admin"));
//        System.out.println(somahoras.somarHorasFuncionarioProjeto(linhaPlanilha, "Nickolas", "Admin"));
//        System.out.println(planilha.carregaFuncionarios(linhaPlanilha).toString());
//        System.out.println(planilha.carregaProjetos(linhaPlanilha).toString());
//        
    }

    private static boolean verificaExtensao(File file) {
        return FilenameUtils.getExtension(file.getName()).toLowerCase().equals("xls") || FilenameUtils.getExtension(file.getName()).toLowerCase().equals("xlsx");
    }

}
