package com.su.excel.core;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * 预处理excel数据，验证数据完整性后保存
 */
@Component
public class PreProcessData implements ApplicationListener<ContextRefreshedEvent> {
	
	
	@Autowired
	private ExcelManager excelManager;

	@Value("${excel.dir}")
	private String dir;

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		File file = new File(dir);
		if (!file.exists()) {
			System.out.println("目录不存在");
			return;
		}
		if (!file.isDirectory()) {
			System.out.println("该路径不是目录");
		}
		for (File f : file.listFiles()) {
			if (f.getName().endsWith("xlsx")) {
				String mappingName = f.getName().substring(0, f.getName().lastIndexOf("."));

				if (!excelManager.contains(mappingName))
					continue;
				ExcelMapping<?> mapping = excelManager.get(mappingName);
				try {
					XSSFWorkbook workbook = new XSSFWorkbook(dir + f.getName());
					XSSFSheet sheet = workbook.getSheetAt(0);
					Iterator<Row> rowIt = sheet.rowIterator();
					Map<Integer, String> title = new HashMap<>();// <列数，列名>
					int rowNum = 0; // 行数
					while (rowIt.hasNext()) {
						Row row = rowIt.next();
						if (++rowNum < 3)
							continue; // 跳过行头
						Iterator<Cell> cellIt = row.cellIterator();
						int columnNum = 0; // 列数
						RowData rowData = null;
						while (cellIt.hasNext()) {
							++columnNum;
							String value = null;
							Cell cell = cellIt.next();
							switch (cell.getCellTypeEnum()) {
							case NUMERIC:
								value = String.valueOf(cell.getNumericCellValue());
								break;
							case STRING:
								value = cell.getStringCellValue();
								break;
							case FORMULA:
								switch (cell.getCachedFormulaResultTypeEnum()) {
								case NUMERIC:
									value = String.valueOf(cell.getNumericCellValue());
									break;
								case STRING:
									value = cell.getStringCellValue();
									break;
								default:
									System.out.println("未知的 formula result type : " + cell.getCellTypeEnum());
									break;
								}
								break;
							default:
								System.out.println("未知的 cell type : " + cell.getCellTypeEnum());
								break;
							}
							if (rowNum == 3) // 保存列名
								title.put(columnNum, value);
							else {
								if (rowData == null)
									rowData = new RowData();
								rowData.put(title.get(columnNum), value);
							}
						}
						if (rowData != null)
							mapping.mapping(rowData);
					}
					Object value = mapping.complete();// 加载完当前表
					excelManager.addPreData(mappingName, value);
					workbook.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}
		excelManager.completeAll(); //加载完所有表
	}

}
