package com.arun.service;

import static com.arun.constants.ExpenseConstants.INPUT_FILE_EXTENSION;
import static com.arun.constants.ExpenseConstants.MONTHS;
import static com.arun.constants.ExpenseConstants.OUTPUT_EXCEL_FILE_EXTENSION;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.ListIterator;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ExpenseMiscService {

	@Value("${expense.input.folder.name}")
	private String inputFolder;

	@Value("${expense.output.folder.name}")
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

	public String getEmailSubject()
	{
		String s="Monthly Expenditure "+getMonth()+" "+getYear();
		
		return s;
	}
	
	public String getClassPathResource(String path)
	{
		try {
			return this.getClass().getResource(path).getPath();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	
	public String getEmailBody(String userName, String senderAddress) {
		String result = "";

		try {
			StringBuffer sb = new StringBuffer();

			List<String> list=Files.readAllLines(Paths.get(getClassPathResource("/static/mail-template.txt")));
			
			ListIterator<String> ite=list.listIterator();
			
			while(ite.hasNext())
			{
			    sb.append(ite.next());
			    sb.append("\n");
			}

			

			result = sb.toString().replaceAll("userName", userName).replaceAll("senderMail", senderAddress)
					.replaceAll("Month", getMonth()).replaceAll("Year", getYear());

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		return result;
	}
	
	
}
