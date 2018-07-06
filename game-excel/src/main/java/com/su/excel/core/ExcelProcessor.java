package com.su.excel.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 预处理excel数据，验证数据完整性后保存
 */
@Component
public class ExcelProcessor {

	@Autowired
	private ExcelContext excelContext;

	@Value("${excel.dir}")
	private String dir;

	public final static String preDataDir = System.getProperty("user.dir") + "/../preData/";

	private static final Logger logger = LoggerFactory.getLogger(ExcelProcessor.class);

	private DataFormatter formatter = new DataFormatter();

	/**
	 * 列名的行数
	 */
	private int cellNameRowNum = 1;

	/**
	 * 预处理数据
	 */
	public void preProcesss() {
		File file = new File(dir);
		if (!file.exists()) {
			logger.error("目录不存在");
			return;
		}
		if (!file.isDirectory()) {
			logger.error("该路径不是目录");
			return;
		}
		for (File f : file.listFiles()) {
			if (f.getName().endsWith("xlsx")) {
				String mapName = f.getName().substring(0, f.getName().lastIndexOf("."));
				ExcelMapper<?> map = excelContext.getExcelMappers().get(mapName);
				if (map == null) {
					logger.error("not find mapper {}", mapName);
					continue;
				}
				try {
					FileInputStream fis = new FileInputStream(dir + f.getName());
					Workbook workbook = null;
					if (f.getName().toLowerCase().endsWith("xlsx")) {
						workbook = new XSSFWorkbook(fis);
					} else if (f.getName().toLowerCase().endsWith("xls")) {
						workbook = new HSSFWorkbook(fis);
					}
					Sheet sheet = workbook.getSheetAt(0);
					Map<Integer, String> title = new HashMap<>();// <列数，列名>
					// 名称行
					Row nameRow = sheet.getRow(cellNameRowNum);
					int columnCount = 0;
					for (Cell cell : nameRow) {
						String cellValue = getCellValueNew(cell);
						title.put(columnCount++, cellValue);
					}
					for (Row row : sheet) {
						if (row.getRowNum() <= cellNameRowNum)
							continue;
						RowData rowData = null;
						for (int i = 0; i < columnCount; i++) {
							Cell cell = row.getCell(i);
							if (cell == null)
								continue;
							String value = getCellValueNew(cell);
							if (rowData == null)
								rowData = new RowData();
							rowData.put(title.get(i), value);
						}
						if (rowData != null) {
							Object rowObject = map.map(rowData);
							if (rowObject == null)
								continue;
							excelContext.addPreData(mapName, rowObject);
						}
					}
					if (excelContext.isEmpty(mapName)) {
						logger.error("{} is empty", mapName);
						continue;
					}
					map.finishLoad();// 加载完当前表
					fis.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		excelContext.doFinishLoadAll(); // 加载完所有表
		excelContext.savePreData(preDataDir); // 保存预处理数据
	}

	@Deprecated
	public String getCellValue(Cell cell) {
		String value = formatter.formatCellValue(cell);
		switch (cell.getCellTypeEnum()) {
		case STRING:
			value = cell.getRichStringCellValue().getString();
			break;
		case NUMERIC:
			if (DateUtil.isCellDateFormatted(cell)) {
				value = String.valueOf(cell.getDateCellValue());
			} else {
				value = String.valueOf(cell.getNumericCellValue());
			}
			break;
		case BOOLEAN:
			value = String.valueOf(cell.getBooleanCellValue());
			break;
		case FORMULA:
			if (cell.getCachedFormulaResultTypeEnum() == CellType.NUMERIC) {
				if (DateUtil.isCellDateFormatted(cell)) {
					value = String.valueOf(cell.getDateCellValue());
				} else {
					value = String.valueOf(cell.getNumericCellValue());
				}
			}
			break;
		case BLANK:
			break;
		default:
		}
		return value;
	}

	public String getCellValueNew(Cell cell) {
		String value = formatter.formatCellValue(cell);
		switch (cell.getCellTypeEnum()) {
		case FORMULA:
			if (cell.getCachedFormulaResultTypeEnum() == CellType.NUMERIC) {
				if (DateUtil.isCellDateFormatted(cell)) {
					value = String.valueOf(cell.getDateCellValue());
				} else {
					value = String.valueOf(cell.getNumericCellValue());
				}
			}
			break;
		default:
		}
		return value;
	}

	/**
	 * 加载数据
	 */
	public void reload() {
		File dir = new File(preDataDir);
		if (!dir.exists()) {
			logger.info("preData 目录不存在");
			return;
		}
		File[] files = dir.listFiles();
		for (File file : files) {
			if (!file.isFile() || !excelContext.getExcelMappers().containsKey(file.getName()))
				continue;
			ExcelMapper<?> map = excelContext.getExcelMappers().get(file.getName());
			try {
				BufferedReader reader = new BufferedReader(new FileReader(file));
				String line = reader.readLine();
				while (line != null) {
					map.add(line);
					line = reader.readLine();
				}
				reader.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		logger.info("加载Excel配置成功");
	}

}
