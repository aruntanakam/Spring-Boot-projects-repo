package com.arun.service;

import static com.arun.constants.ExpenseConstants.EXPENSES_COST_MAP;
import static com.arun.constants.ExpenseConstants.EXPENSES_NAMES_MAP;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.arun.Iservice.IExpenseCalculatorService;
import com.arun.Iservice.IExpenseGeneratorService;

@Service("excelService")
public class ExpenseExcelService implements IExpenseGeneratorService {
	

	@Autowired
	private IExpenseCalculatorService calculatorService;
	
	@Autowired
	private ExpenseMiscService miscService;
	
	
	public void deleteFileIfExists(String file)
	{
		try {
			Files.deleteIfExists(Paths.get(file));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public String generateFile() {
		
		Map<String, Object> map=calculatorService.calculateExpense();
		
		
		if(map!=null)
		{
	
		Map<String,List<String>> expensesNamesMap=(Map<String, List<String>>) map.get(EXPENSES_NAMES_MAP);
		
		Map<String,List<Double>> expensesCostMap=(Map<String, List<Double>>) map.get(EXPENSES_COST_MAP);
		
		String outputfile=miscService.getOutputExcelFileName();
		
		deleteFileIfExists(outputfile);
		
		try(Workbook wb=new XSSFWorkbook();FileOutputStream os=new FileOutputStream(outputfile) )
		{
			Sheet sh=wb.createSheet(miscService.getMonth()+" "+miscService.getYear()+" expenses");
			
			Row row=sh.createRow(0);
			
			row.createCell(0).setCellValue("Expense Date");
			row.createCell(1).setCellValue("Expense Name");
			row.createCell(2).setCellValue("Expense Amount");
			
			int r=1;
			
		for(Map.Entry<String,List<String>> entry:expensesNamesMap.entrySet())
		{
			String date=entry.getKey();
			
			List<String> expensesName=entry.getValue();
			
			List<Double> expensesAmount=expensesCostMap.get(date);
			
			for(int i=0;i<expensesName.size();i++)
			{
				row=sh.createRow(r);
				row.createCell(0).setCellValue(date);
				row.createCell(1).setCellValue(expensesName.get(i));
				row.createCell(2).setCellValue(expensesAmount.get(i).doubleValue());
				r++;
			}
			
		}
		
		wb.write(os);
			
		return "File "+outputfile+" generated on "+(LocalDateTime.now().toString());	
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
		
		}
		else
		{
			return null;
		}
		
		
		
	}

	
	
}
