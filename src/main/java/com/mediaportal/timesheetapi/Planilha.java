package com.mediaportal.timesheetapi;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

public class Planilha {
    
//    public XSSFSheet lerXLSX(String basepath) throws FileNotFoundException {
//        FileInputStream file = new FileInputStream(new File(basepath));
//        XSSFWorkbook wb = new XSSFWorkbook(file);
//        return wb.getSheetAt(0);
//    }
    
    public HSSFSheet lerXLS(String path) throws FileNotFoundException, IOException {
        FileInputStream file = new FileInputStream(new File(path));
        HSSFWorkbook wb = new HSSFWorkbook(file);
        return wb.getSheetAt(0);
    }
    
    public String lerCelula(Sheet sheet, int linha, int coluna) throws FileNotFoundException, IOException {
        Row row = sheet.getRow(linha);
        Cell cell = (Cell) row.getCell(coluna);
        return cell.getStringCellValue();
    }
    
    public List<Valores> salvarDados(String path) throws IOException {
        Sheet sheet = lerXLS(path);
        List<Valores> celula = new ArrayList();
        String teste = "";
        int linha = 0;
        int numeroDeLinhas = 0;
        while(teste.toUpperCase() != "TOTAL"){
            teste = lerCelula(sheet, linha, 0);
            linha++;
            if(teste == "TOTAL") {
                numeroDeLinhas = linha;
            }
        }
        for(int i = 3; i < 10; i++) {
            for(int j = 1; j < numeroDeLinhas; j++) {
                String lerCelula = lerCelula(sheet, j, i);
                if(lerCelula != "0:00" || lerCelula != "00:00" || lerCelula != "*") {
                    Valores valores = new Valores();
                    valores.setFuncionario(lerCelula(sheet, 4, 2));
                    valores.setData(lerCelula(sheet, 5, i));
                    valores.setHorasTrabalhadas(lerCelula(sheet, j, i));
                    valores.setIdProjeto(lerCelula(sheet, j, 1));
                    valores.setProjeto(lerCelula(sheet, j, 2));
                    celula.add(valores);
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
        for(int i = 0; i < linhas.size(); i++) {
            funcionario.add(linhas.get(i).getFuncionario());
        }
        return funcionario;
    }
    
    public Set<String> carregaProjetos(List<Valores> linhas) {
        Set<String> projetos = new HashSet<String>();
        for(int i = 0; i < linhas.size(); i++) {
            projetos.add(linhas.get(i).getProjeto());
        }
        return projetos;
    }

}
