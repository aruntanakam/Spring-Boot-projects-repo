package com.expenso.service;

import static com.expenso.constants.ExpenseConstants.INPUT_FILE_EXTENSION;
import static com.expenso.constants.ExpenseConstants.MONTHS;
import static com.expenso.constants.ExpenseConstants.OUTPUT_EXCEL_FILE_EXTENSION;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.ListIterator;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.expenso.entity.MonthAndYearInput;

@Service
public class ExpenseMiscService {

	@Value("${expense.input.folder.name}")
	private String inputFolder;

	@Value("${expense.output.folder.name}")
	private String outputFolder;
	

	public String getInputFileName(MonthAndYearInput input) {

		return inputFolder + input.getMonth() + "_" + input.getYear() + INPUT_FILE_EXTENSION;

	}
	
	public String getOutputExcelFileName(MonthAndYearInput input)
	{
		return outputFolder + input.getMonth() + "_" + input.getYear() + OUTPUT_EXCEL_FILE_EXTENSION;
	}

	public int getMonthValue() {
		LocalDate date = LocalDate.now();
		int temp = date.getMonthValue() - 1;
		return (temp == 0) ? 12 : temp;
	}

	public String getMonth() {
		
		

		return (MONTHS[getMonthValue() - 1]);
		

	}

	public String getYear() {

		int temp = LocalDate.now().getYear();

		int year = (getMonthValue() == 12) ? (temp - 1) : temp;

		return Integer.toString(year);
	}

	public String getEmailSubject(MonthAndYearInput input)
	{
		String s="Monthly Expenditure "+input.getMonth()+" "+input.getYear();
		
		return s;
	}
	
	
	
}
