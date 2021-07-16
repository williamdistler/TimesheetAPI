package com.mediaportal.timesheetapi;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Planilha {

    public List<Valores> carregaValoresDoExcelEmLista(String path) throws IOException {

        File file = new File(path);
        FileInputStream reader = new FileInputStream(file);
        Valores valores = new Valores();
        Workbook wb = null;
        String extension = FilenameUtils.getExtension(path);
        
        String filename = FilenameUtils.getName(path);
        
        valores.setNomeArquivo(filename);
        
        if (extension.toUpperCase().equals("XLS")) {
            try {
                wb = new HSSFWorkbook(reader);
            } catch (Exception e) {
                Relatorio.errors.add("Não foi possivel abrir o arquivo: " + filename);
                return Collections.emptyList();
            }
        } else if (extension.toUpperCase().equals("XLSX")) {
            try {
                wb = new XSSFWorkbook(reader);
            } catch (Exception e) {
                Relatorio.errors.add("Não foi possivel abrir o arquivo: " + filename);
                return Collections.emptyList();
            }

        }
        Sheet sheet = wb.getSheetAt(0);
        
        int numLinhaInicialExcel = getLinhasInicialDoExcel(sheet);
        int numLinhaFinalExcel = getLinhasFinalDoExcel(sheet);

        if (numLinhaFinalExcel == 0 || numLinhaInicialExcel == 0) {
            Relatorio.errors.add("Relatório com formato inválido: " + filename);
            return Collections.emptyList();
        }

        List<Valores> valoresDoExcel = carregaDados(sheet, filename ,numLinhaInicialExcel, numLinhaFinalExcel);
        return valoresDoExcel;
    }

    private List<Valores> carregaDados(Sheet sheet, String filename, int numLinhaInicial, int numLinhaFinal) throws IOException {
        List<Valores> listaDeValoresDoExcel = new ArrayList();

        //System.out.println(numeroDeLinhas);
        for (int coluna = 2; coluna < 9; coluna++) {
            String nomeFuncionario = null;
            Double doubleData = null;
            Double doubleHorasTrabalhadas;
            String idProjeto;
            String projeto;

            for (int linha = numLinhaInicial; linha < numLinhaFinal; linha++) {
                Double lerCelulaDouble = lerCelulaDouble(sheet, linha, coluna);
                String lerCelulaString = lerCelulaString(sheet, linha, coluna);
                //System.out.println("double " + lerCelulaDouble + "String " + lerCelulaString);
                if (lerCelulaDouble != 0.0 && !lerCelulaString.equals("*")) {
                    try {
                        nomeFuncionario = lerCelulaString(sheet, 3, 1);
                        doubleData = lerCelulaDouble(sheet, numLinhaInicial - 1, coluna);
                        doubleHorasTrabalhadas = lerCelulaDouble(sheet, linha, coluna);
                        idProjeto = lerCelulaString(sheet, linha, 0);
                        projeto = lerCelulaString(sheet, linha, 1);

                        listaDeValoresDoExcel.addAll(insereValoresNaLista(nomeFuncionario, doubleData, doubleHorasTrabalhadas, idProjeto, projeto));
                    } catch (Exception e) {
                        Relatorio.errors.add("Erro na coluna: " + (coluna + 1) + " linha: " + (linha + 1) + " da planilha: " + filename);
                        return null;
                    }
                }
            }
            String totalDia = Utils.getHorasHHMM(lerCelulaDouble(sheet, numLinhaFinal, coluna));
            Integer horas = Integer.parseInt(totalDia.split(":")[0]);
            if (doubleData != null && horas <= 2 || horas >= 10) {
                Relatorio.warnings.add("No dia " + Utils.formatDoubleToDate(doubleData) + ", o funcionario " + nomeFuncionario + " trabalhou " + totalDia + " horas. Arquivo: " + filename);
            }
        }

        return listaDeValoresDoExcel;
    }

    private List<Valores> insereValoresNaLista(String nomeFuncionario, Double doubleData, Double doubleHorasTrabalhadas, String idProjeto, String projeto) {
        List<Valores> listaDeValoresDoExcel = new ArrayList();
        Valores valores = new Valores();

        Date data = null;
        String dataFormatada = "";

        try {
            dataFormatada = Utils.formatDoubleToDate(doubleData);
        } catch (Exception e) {
            Relatorio.errors.add("Data inválida ");
            return Collections.emptyList();
        }

        String horasTrabalhadas = Utils.getHorasHHMM(doubleHorasTrabalhadas);

        valores.setFuncionario(nomeFuncionario);
        valores.setData(dataFormatada);
        valores.setHorasTrabalhadas(horasTrabalhadas);
        valores.setIdProjeto(idProjeto);
        valores.setProjeto(projeto);
        listaDeValoresDoExcel.add(valores);
        return listaDeValoresDoExcel;
        //System.out.println("FUNC: " + valores.getFuncionario() + "DATA: " + valores.getData() + " HORAS: " + valores.getHorasTrabalhadas() + " " + valores.getIdProjeto() + " " + valores.getProjeto());
    }

    private int getLinhasInicialDoExcel(Sheet sheet) throws IOException {
        int linhaAtual = 0;
        String valorDaCelula = "";
        int numeroDeLinhas = 0;
        while (true) {
            valorDaCelula = lerCelulaString(sheet, linhaAtual, 9).toUpperCase();
            linhaAtual++;
            if (valorDaCelula.equalsIgnoreCase("TOTAL")) {
                numeroDeLinhas = linhaAtual - 1;
                break;
            }
            if (linhaAtual == 50) {
                numeroDeLinhas = 0;
                break;
            }
        }
        return numeroDeLinhas;
    }

    private int getLinhasFinalDoExcel(Sheet sheet) throws IOException {
        int linhaAtual = 0;
        String valorDaCelula = "";
        int numeroDeLinhas = 0;
        while (true) {
            valorDaCelula = lerCelulaString(sheet, linhaAtual, 0).toUpperCase();
            linhaAtual++;
            if (valorDaCelula.equalsIgnoreCase("TOTAL")) {
                numeroDeLinhas = linhaAtual - 1;
                break;
            }
            if (linhaAtual == 50) {
                numeroDeLinhas = 0;
                break;
            }
        }
        return numeroDeLinhas;
    }

    private String lerCelulaString(Sheet sheet, int linha, int coluna) throws FileNotFoundException, IOException {
        Row row = sheet.getRow(linha);
        if (row == null) {
            return "";
        }
        Cell cell = row.getCell(coluna);
        if (cell != null && cell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
            return cell.getStringCellValue();
        }
        return "";
    }

    private Double lerCelulaDouble(Sheet sheet, int linha, int coluna) throws FileNotFoundException, IOException {
        Row row = sheet.getRow(linha);
        if (row == null) {
            return 0.0;
        }
        Cell cell = row.getCell(coluna);
        if (cell != null && (cell.getCellType() == HSSFCell.CELL_TYPE_FORMULA || cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC)) {
            return cell.getNumericCellValue();
        }
        return 0.0;
    }

    @Deprecated
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
}
