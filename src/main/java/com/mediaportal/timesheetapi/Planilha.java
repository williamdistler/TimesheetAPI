package com.mediaportal.timesheetapi;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class Planilha {

//    public XSSFSheet lerXLSX(String basepath) throws FileNotFoundException {
//        FileInputStream file = new FileInputStream(new File(basepath));
//        XSSFWorkbook wb = new XSSFWorkbook(file);
//        return wb.getSheetAt(0);
//    }
    public String lerCelulaString(HSSFSheet sheet, int linha, int coluna) throws FileNotFoundException, IOException {
        System.out.println("LerCelula");
        HSSFRow row = sheet.getRow(linha);
        System.out.println("row: " + row + "    coluna: " + coluna + "linha: " + linha);
        if (row == null) {
            return "";
        }
        HSSFCell cell = row.getCell(coluna);
        if (cell != null) {
            System.out.println("celula " + cell.getRichStringCellValue());
            System.out.println(cell.getStringCellValue());
            return cell.getStringCellValue();
        }
        return "";
    }
    
        public Date lerCelulaData(HSSFSheet sheet, int linha, int coluna) throws FileNotFoundException, IOException {
        System.out.println("LerCelula");
        HSSFRow row = sheet.getRow(linha);
        System.out.println("row: " + row + "    coluna: " + coluna + "linha: " + linha);
        if (row == null) {
            return null;
        }
        HSSFCell cell = row.getCell(coluna);
        if (cell != null) {
            System.out.println("celula " + cell.getRichStringCellValue());
            System.out.println(cell.getStringCellValue());
            return cell.getDateCellValue();
        }
        return null;
    }

    public List<Valores> salvarDados(String path) throws IOException {
        FileInputStream file = new FileInputStream(new File(path));
        HSSFWorkbook wb = new HSSFWorkbook(file);
        HSSFSheet sheet = wb.getSheetAt(0);
        List<Valores> celula = new ArrayList();
        String teste = "";
        int linha = 1;
        int numeroDeLinhas = 0;
        while (teste.toUpperCase() != "TOTAL") {
            teste = lerCelulaString(sheet, linha, 0);
            linha++;
            if (teste.equalsIgnoreCase("TOTAL")) {
                numeroDeLinhas = linha;
                break;
            }
        }
        System.out.println(numeroDeLinhas);
        for (int i = 3; i < 10; i++) {
            for (int j = 6; j < numeroDeLinhas; j++) {
                String lerCelula = lerCelulaString(sheet, j, i);
                if (lerCelula != "0:00" || lerCelula != "00:00" || lerCelula != "*") {
                    Valores valores = new Valores();
                    valores.setFuncionario(lerCelulaString(sheet, 4, 2));
                    valores.setData(lerCelulaString(sheet, 5, i));
                    valores.setHorasTrabalhadas(lerCelulaString(sheet, j, i));
                    valores.setIdProjeto(lerCelulaString(sheet, j, 1));
                    valores.setProjeto(lerCelulaString(sheet, j, 2));
                    celula.add(valores);
                    System.out.println(valores.getData() + valores.getFuncionario() + valores.getHorasTrabalhadas() + valores.getIdProjeto() + valores.getProjeto());
                }
            }
        }
        return celula;
    }

    public List<Valores> lerPlanilhaOld(String path) {

        List<Valores> listaPlanilha = new ArrayList();
        List<String> itemLista = new ArrayList();
        String linha;

        try {

            BufferedReader leitor = new BufferedReader(new FileReader(path));

            while ((linha = leitor.readLine()) != null) {

                Valores linhaPlanilha = new Valores();
                itemLista.add(linha);
                String[] separaLinha = linha.split("\\|");

                linhaPlanilha.setFuncionario(separaLinha[0]);
                linhaPlanilha.setData(separaLinha[1]);
                linhaPlanilha.setHorasTrabalhadas(separaLinha[2]);
                linhaPlanilha.setIdProjeto(separaLinha[3]);
                linhaPlanilha.setProjeto(separaLinha[4]);
                listaPlanilha.add(linhaPlanilha);

            }

        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }

        return listaPlanilha;

    }

    public Set<String> carregaFuncionarios(List<Valores> linhas) {
        Set<String> funcionario = new HashSet<String>();
        for (int i = 0; i < linhas.size(); i++) {
            funcionario.add(linhas.get(i).getFuncionario());
        }
        return funcionario;
    }

    public Set<String> carregaProjetos(List<Valores> linhas) {
        Set<String> projetos = new HashSet<String>();
        for (int i = 0; i < linhas.size(); i++) {
            projetos.add(linhas.get(i).getProjeto());
        }
        return projetos;
    }

}
