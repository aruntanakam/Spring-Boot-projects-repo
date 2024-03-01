package com.arun.service;

import static com.arun.constants.ExpenseConstants.EXCEL_HEADERS;
import static com.arun.constants.ExpenseConstants.EXPENSES_COST_MAP;
import static com.arun.constants.ExpenseConstants.EXPENSES_NAMES_MAP;
import static com.arun.constants.ExpenseConstants.EXPENSES_TOTAL_MAP_DAILY;
import static com.arun.constants.ExpenseConstants.TOTAL_MAP;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.apache.poi.poifs.crypt.HashAlgorithm;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.arun.Iservice.IExpenseCalculatorService;
import com.arun.Iservice.IExpenseGeneratorService;

import jakarta.mail.internet.MimeMessage;

@Service("excelService")
public class ExpenseExcelService implements IExpenseGeneratorService {

	@Autowired
	private IExpenseCalculatorService calculatorService;
	
	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private ExpenseMiscService miscService;

	public void deleteFileIfExists(String file) {
		try {
			Files.deleteIfExists(Paths.get(file));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public XSSFCellStyle getHeaderStyle(XSSFWorkbook wb) {
		XSSFCellStyle headerStyle = wb.createCellStyle();

		headerStyle.setAlignment(HorizontalAlignment.CENTER);

    XSSFFont font=wb.createFont();
 		
		font.setFontHeightInPoints((short)18);
		
		font.setBold(true);
		headerStyle.setFont(font);

		headerStyle.setFont(font);
		headerStyle.setBorderBottom(BorderStyle.THICK);
		headerStyle.setBorderRight(BorderStyle.THICK);
		headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		headerStyle.setBottomBorderColor(IndexedColors.RED.index);
		headerStyle.setRightBorderColor(IndexedColors.RED.index);
		headerStyle.setFillBackgroundColor(IndexedColors.YELLOW.index);
		headerStyle.setFillForegroundColor(IndexedColors.YELLOW.index);
		headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

		// headerStyle.setFillPattern(FillPatternType.THICK_BACKWARD_DIAG);
		return headerStyle;
	}

	public void createHeaderCell(XSSFWorkbook wb, XSSFRow row, String cellName, int columnNo) {
		XSSFCell cell = row.createCell(columnNo);

		cell.setCellValue(cellName);
		cell.setCellStyle(getHeaderStyle(wb));
	}

	public void createHeader(XSSFWorkbook wb, XSSFSheet sh) {
		XSSFRow row = sh.createRow(0);

		row.setHeight((short) 1000);

		for (int i = 0; i < EXCEL_HEADERS.length; i++) {
			createHeaderCell(wb, row, EXCEL_HEADERS[i], i);
			sh.setColumnWidth(i, 10000);
		}

	}

	public void createBoder(XSSFWorkbook wb) {
		XSSFCellStyle style = wb.createCellStyle();

		style.setBorderBottom(BorderStyle.THICK);
	}

	public void createCell(String value, XSSFWorkbook wb, XSSFSheet sh, XSSFRow r, String type, int number) {
		XSSFCell cell = r.createCell(number);

		cell.setCellValue(value);

		XSSFCellStyle style = wb.createCellStyle();
		cell.setCellStyle(style);
		style.setAlignment(HorizontalAlignment.CENTER);
		style.setVerticalAlignment(VerticalAlignment.CENTER);
		style.setBorderRight(BorderStyle.THICK);
		style.setRightBorderColor(IndexedColors.RED.index);

		switch (type) {

		case "last":
			style.setBorderBottom(BorderStyle.THICK);
			style.setBottomBorderColor(IndexedColors.RED.index);

			break;

		default:
			break;

		}

	}

	public void createFooter(XSSFWorkbook wb, XSSFSheet sheet, int r, double total) {
		String message = "Your total expenditure for the month of " + miscService.getMonth() + " is " + total;
		r = r + 3;
		XSSFRow row = sheet.createRow(r);
		row.setHeight((short) 1000);

		XSSFCellStyle headerStyle = wb.createCellStyle();

		headerStyle.setAlignment(HorizontalAlignment.CENTER);
	
		XSSFFont font=wb.createFont();
		
		font.setFontHeightInPoints((short)22);
		font.setBold(true);
		headerStyle.setFont(font);
	

		headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);

		headerStyle.setFillBackgroundColor(IndexedColors.YELLOW.index);
		headerStyle.setFillForegroundColor(IndexedColors.YELLOW.index);
		headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

		XSSFCell cell1 = row.createCell(1);
		cell1.setCellStyle(headerStyle);

		cell1.setCellValue(message);

		sheet.addMergedRegion(new CellRangeAddress(r, r, 1, 3));

	}

	@SuppressWarnings({ "unchecked", "unused" })
	@Override
	public String generateFile() {

		Map<String, Object> map = calculatorService.calculateExpense();

		double total = (double) map.get(TOTAL_MAP);

		Map<String, Double> total_daily = (Map<String, Double>) map.get(EXPENSES_TOTAL_MAP_DAILY);

		if (map != null) {

			Map<String, List<String>> expensesNamesMap = (Map<String, List<String>>) map.get(EXPENSES_NAMES_MAP);

			Map<String, List<Double>> expensesCostMap = (Map<String, List<Double>>) map.get(EXPENSES_COST_MAP);

			String outputfile = miscService.getOutputExcelFileName();

			deleteFileIfExists(outputfile);
			int r = 1;

			try (XSSFWorkbook wb = new XSSFWorkbook(); FileOutputStream os = new FileOutputStream(outputfile)) {
				XSSFSheet sh = wb.createSheet(miscService.getMonth() + " " + miscService.getYear() + " expenses");

				createHeader(wb, sh);

				for (Map.Entry<String, List<String>> entry : expensesNamesMap.entrySet()) {
					String date = entry.getKey();

					List<String> expensesName = entry.getValue();

					List<Double> expensesAmount = expensesCostMap.get(date);

					int temp = r;
					XSSFRow row = null;
					int i = 0;
					for (; i < expensesName.size() - 1; i++) {
						row = sh.createRow(r);
						createCell(date, wb, sh, row, "", 0);
						createCell(expensesName.get(i), wb, sh, row, "", 1);
						createCell(expensesAmount.get(i).toString(), wb, sh, row, "", 2);
						createCell(total_daily.get(date).toString(), wb, sh, row, "", 3);
						r++;
					}
					row = sh.createRow(r);
					createCell(date, wb, sh, row, "last", 0);
					createCell(expensesName.get(i), wb, sh, row, "last", 1);
					createCell(expensesAmount.get(i).toString(), wb, sh, row, "last", 2);
					createCell(total_daily.get(date).toString(), wb, sh, row, "last", 3);
					r++;

					if (expensesName.size() > 1) {

						sh.addMergedRegion(new CellRangeAddress(temp, r - 1, 0, 0));
						sh.addMergedRegion(new CellRangeAddress(temp, r - 1, 3, 3));

					}

				}
				createFooter(wb, sh, r, total);
				wb.setWorkbookPassword("01072000", HashAlgorithm.sha256);
				wb.write(os);
				
				

				return "File " + outputfile + " generated on " + (LocalDateTime.now().toString());
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
			

		} else {
			return null;
		}

	}

}
