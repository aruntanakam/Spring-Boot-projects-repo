package com.arun.service;

import static com.arun.constants.ExpenseConstants.INPUT_FILE_EXTENSION;
import static com.arun.constants.ExpenseConstants.MONTHS;
import static com.arun.constants.ExpenseConstants.OUTPUT_EXCEL_FILE_EXTENSION;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ExpenseMiscService {

	@Value("${expense.input.folder.name}")
	private String inputFolder;

	@Value("${expense.input.folder.name}")
	private String outputFolder;

	public String getInputFileName() {

		return inputFolder + getMonth() + "_" + getYear() + INPUT_FILE_EXTENSION;

	}
	
	public String getOutputExcelFileName()
	{
		return outputFolder + getMonth() + "_" + getYear() + OUTPUT_EXCEL_FILE_EXTENSION;
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

	
}
