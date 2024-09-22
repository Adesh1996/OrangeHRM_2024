package com.OrangeHRM.utilities;

import java.io.File;
import java.io.FileReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.opencsv.CSVReader;

public class CSVUtilities {

	private File file = null;
	private Map<String, List<String>> csvDataMap = null;

	public CSVUtilities(String csvFilePath) {
		file = new File(csvFilePath);
		csvDataMap = new HashMap<>();
	}

	/**
	 * Returns an object of map in the form Map<String, List<String>> with CSV
	 * headers as key and corresponding data as values list mapped to keys.
	 * 
	 * @param exportedCSVFilePath
	 * @return CSV data as map
	 * 
	 * @author Adesh.Khedkar
	 * @return
	 **/
	public Map<String, List<String>> readCsvFileAsMap() {
		List<String> mapKeys = new ArrayList<>();
		List<String> rowValues = new ArrayList<>();
		try {
			FileReader fileReader = new FileReader(file);
			try (CSVReader csvReader = new CSVReader(fileReader)) {
				String[] rowData;
				int rowIndex = 1;
				while ((rowData = csvReader.readNext()) != null) {

					for (String cellData : rowData) {
						cellData = formatCsvCellData(cellData.trim());
						if (rowIndex == 1) {
							mapKeys.add(formatCsvCellData(cellData));
						} else {
							rowValues.add(formatCsvCellData(cellData));
						}
					}
					putRowValuesInCSVMap(mapKeys, rowValues);
					rowValues = new ArrayList<>();
					rowIndex++;
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// Removing Unwanted Key
		if (csvDataMap.containsKey("Grouped by")) {
			csvDataMap.remove("Grouped by");
		}
		return csvDataMap;
	}

	private void putRowValuesInCSVMap(List<String> mapKeys, List<String> rowValues) {
		List<String> tempList = new ArrayList<>();

		if (!rowValues.isEmpty()) {
			for (int i = 0; i < mapKeys.size(); i++) {
				if (csvDataMap.get(mapKeys.get(i)) != null) {
					tempList = csvDataMap.get(mapKeys.get(i));
				}
				tempList.add(rowValues.get(i));
				csvDataMap.put(mapKeys.get(i), tempList);
				tempList = new ArrayList<>();
			}
		}
	}

	private String formatCsvCellData(String cellData) {
		if (cellData.contains("\n"))
			cellData = cellData.substring(cellData.lastIndexOf("\n") + 1);
		if (cellData.contains("\""))
			cellData = cellData.replace("\"", "");
		if (cellData.equals("Account/ I d number"))
			cellData = cellData.replaceAll("Account/ I d number", "Account/id number");
		if (cellData.contains(".00"))
			cellData = cellData.replace(".00", "");
		if (cellData.contains("'"))
			cellData = cellData.replace("'", "");
		if (cellData.contains(".")) {
			try {
				BigDecimal decimal = new BigDecimal(cellData).stripTrailingZeros();
				cellData = decimal.toPlainString();
			} catch (NumberFormatException e) {
				// Do nothing if cellData is not a valid number
			}
		}
		return cellData;
	}

	/*
	 * public static void main(String[] args) { CSVUtilities xyz = new CSVUtilities(
	 * "C:\\Users\\ADMIN\\Desktop\\Adesh Khedkar\\123\\MasterCard\\economic-survey-of-manufacturing-june-2023-quarter-csv.csv"
	 * ); System.out.println(xyz.readCsvFileAsMap()); }
	 */

}
