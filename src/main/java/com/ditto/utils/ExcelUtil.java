package com.ditto.utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Hashtable;

public class ExcelUtil {

	public static Hashtable<String, String> getTestDataAsHashtable(String filePath, String sheetName) {
		Hashtable<String, String> dataTable = new Hashtable<>();
		FileInputStream fis = null;
		Workbook workbook = null;
		try {
			fis = new FileInputStream(filePath);
			workbook = new XSSFWorkbook(fis);
			Sheet sheet = workbook.getSheet(sheetName);
			Row headerRow = sheet.getRow(0);
			Row valueRow = sheet.getRow(1);
			int cellCount = headerRow.getLastCellNum();
			DataFormatter formatter = new DataFormatter();
			for (int i = 0; i < cellCount; i++) {
				String key = formatter.formatCellValue(headerRow.getCell(i));
				String value = formatter.formatCellValue(valueRow.getCell(i));
				System.out.println(key + " : " + value);
				dataTable.put(key, value);
			}
		} catch (Exception e) {
			throw new RuntimeException("Error reading Excel file: " + e.getMessage());
		} finally {
			try {
				if (workbook != null)
					workbook.close();
				if (fis != null)
					fis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return dataTable;
	}
}