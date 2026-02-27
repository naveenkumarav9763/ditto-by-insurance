package com.ditto.utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Hashtable;

public class ExcelUtil {

	/**
	 * Reads test data from given Excel sheet and returns it as a key-value
	 * Hashtable.
	 * first row contains headers and second row contains values.
	 *
	 * @param filePath  path of the Excel file
	 * @param sheetName name of the sheet
	 * @return Hashtable containing test data
	 */
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