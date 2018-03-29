package com.su.excel.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
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
	private ExcelManager excelManager;

	@Value("${excel.dir}")
	private String dir;

	@Value("${excel.preData.dir}")
	private String preDataDir;

	private static final Logger logger = LoggerFactory.getLogger(ExcelProcessor.class);

	/**
	 * 列名的行数
	 */
	private int cellNameRowNum = 3;

	/**
	 * 预处理数据
	 */
	public void preProcesss() {
		File file = new File(dir);
		if (!file.exists()) {
			logger.info("目录不存在");
			return;
		}
		if (!file.isDirectory()) {
			logger.info("该路径不是目录");
			return;
		}
		for (File f : file.listFiles()) {
			if (f.getName().endsWith("xlsx")) {
				String mappingName = f.getName().substring(0, f.getName().lastIndexOf("."));
				if (!excelManager.contains(mappingName))
					continue;
				ExcelMapping<?> mapping = excelManager.get(mappingName);
				try {
					OPCPackage pkg = OPCPackage.open(new File(dir + f.getName()));
					XSSFWorkbook workbook = new XSSFWorkbook(pkg);
					XSSFSheet sheet = workbook.getSheetAt(0);
					Map<Integer, String> title = new HashMap<>();// <列数，列名>
					int rowNum = 0; // 行数
					DataFormatter formatter = new DataFormatter();
					for (Row row : sheet) {
						System.out.println();
						if (++rowNum < cellNameRowNum)
							continue; // 跳过行头
						int columnNum = 0; // 列数
						RowData rowData = null;
						for (Cell cell : row) {
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
								value = String.valueOf(cell.getCellFormula());
								break;
							case BLANK:
								break;
							default:
							}
							++columnNum;
							System.out.print(value + " ");
							if (rowNum == cellNameRowNum) // 保存列名
								title.put(columnNum, value);
							else {
								if (rowData == null)
									rowData = new RowData();
								rowData.put(title.get(columnNum), value);
							}
						}
						if (rowData != null) {
							// Object rowObject = mapping.mapping(rowData);
							// excelManager.addPreData(mappingName, rowObject);
						}
					}
					mapping.complete();// 加载完当前表
					workbook.close();
					pkg.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}
		excelManager.completeAll(); // 加载完所有表
		excelManager.savePreData(); // 保存预处理数据
	}

	/**
	 * 刷新配置
	 */
	public void refresh() {
		File dir = new File(preDataDir);
		if (!dir.exists()) {
			logger.info("preData 目录不存在");
			return;
		}
		File[] files = dir.listFiles();
		for (File file : files) {
			if (!file.isFile() || !excelManager.contains(file.getName()))
				continue;
			ExcelMapping<?> mapping = excelManager.get(file.getName());
			try {
				BufferedReader reader = new BufferedReader(new FileReader(file));
				String line = reader.readLine();
				while (line != null) {
					mapping.add(line);
					line = reader.readLine();
				}
				reader.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
