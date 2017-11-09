package com.su.excel.core;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.su.excel.core.type.TypeManager;

/**
 * 预处理excel数据，验证数据完整性后保存
 */
@Component
public class PreProcessData implements ApplicationListener<ContextRefreshedEvent> {

	@Autowired
	private ExcelManager excelManager;

	@Autowired
	private TypeManager typeManager;

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

				if (excelManager.contains(mappingName)) {
					try {
						XSSFWorkbook workbook = new XSSFWorkbook(dir + f.getName());
						XSSFSheet sheet = workbook.getSheetAt(0);
						Iterator<Row> rowIt = sheet.rowIterator();
						while (rowIt.hasNext()) {
							Row row = rowIt.next();
							Iterator<Cell> cellIt = row.cellIterator();
							while (cellIt.hasNext()) {
								Cell cell = cellIt.next();
								switch (cell.getCellTypeEnum()) {
								case NUMERIC:
									System.out.print(cell.getNumericCellValue() + " \t\t ");
									break;
								case STRING:
									System.out.print(cell.getStringCellValue() + " \t\t ");
									break;
								case FORMULA:
									System.out.print(cell.getCachedFormulaResultTypeEnum() + " \t\t ");
									break;
								case BLANK:
									break;
								default:
									System.out.println(cell.getCellTypeEnum());
									break;
								}
							}
							System.out.println();
						}
						workbook.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	

}
