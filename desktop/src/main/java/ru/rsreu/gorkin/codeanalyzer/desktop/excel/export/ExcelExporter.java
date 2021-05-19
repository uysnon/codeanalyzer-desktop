package ru.rsreu.gorkin.codeanalyzer.desktop.excel.export;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import ru.rsreu.gorkin.codeanalyzer.core.metrics.Metric;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ExcelExporter {
    public void export(List<Metric> metrics, File fileToSave) {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("Первый лист");

        HSSFRow rowhead = sheet.createRow((short) 0);
        rowhead.createCell(0).setCellValue("Метрика");
        rowhead.createCell(1).setCellValue("Полное название");
        rowhead.createCell(2).setCellValue("Значение");

        for (int i = 0, j = 1; i < metrics.size(); i++, j++) {
            Metric metric = metrics.get(i);
            HSSFRow row = sheet.createRow((short) j);
            row.createCell(0).setCellValue(metric.getTitle());
            row.createCell(1).setCellValue(metric.getDescription());
            row.createCell(2).setCellValue(metric.getCount());
        }

        try (
                FileOutputStream fileOut = new FileOutputStream(fileToSave)
        ) {
            workbook.write(fileOut);
            fileOut.close();
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void export(Map<String, List<Metric>> elementMetrics, File fileToSave) throws IOException {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("Первый лист");

        Set<String> keys = elementMetrics.keySet();
        int rowIndex = 0;
        for (String key : keys) {
            HSSFRow keyRow = sheet.createRow((short) rowIndex++);
            keyRow.createCell(0).setCellValue(key);
            HSSFRow rowhead = sheet.createRow((short) rowIndex++);
            rowhead.createCell(1).setCellValue("Метрика");
            rowhead.createCell(2).setCellValue("Полное название");
            rowhead.createCell(3).setCellValue("Значение");
            List<Metric> metrics = elementMetrics.get(key);
            for (Metric metric : metrics) {
                HSSFRow row = sheet.createRow((short) rowIndex++);
                row.createCell(1).setCellValue(metric.getTitle());
                row.createCell(2).setCellValue(metric.getDescription());
                row.createCell(3).setCellValue(metric.getCount());
            }
        }
        try (
                FileOutputStream fileOut = new FileOutputStream(fileToSave)
        ) {
            workbook.write(fileOut);
            fileOut.close();
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
