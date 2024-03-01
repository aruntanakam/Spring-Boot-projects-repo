package com.arun.controller;

import static com.arun.constants.ExpenseConstants.TOTAL_MAP;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RestController;

import com.arun.Iservice.IExpenseCalculatorService;
import com.arun.Iservice.IExpenseGeneratorService;
import com.arun.service.ExpenseMiscService;

@RestController
public class ExpenseController {
	
	
	@Autowired
	private IExpenseCalculatorService calculatorService;
	
	@Autowired
	@Qualifier("excelService")
	private IExpenseGeneratorService excelService;
	
	@Autowired
	private ExpenseMiscService miscService;
	
	
	
   public String createExcelFile()
   {
	   String result=excelService.generateFile();
	   
	  return (result!=null)?result:"File generation failed";
   }
   
   public String getTotal()
	{
	   
	   Map<String,Object> map=calculatorService.calculateExpense();
	   
	   String result=null;
	   
		if(map!=null)
		{
			double total= (Double)(map.get(TOTAL_MAP));
			
			   result="Total expenditure for the month of "+miscService.getMonth()+" is "+total;
		}
	  
	  return (result!=null)?result:"Expense calculation failed";
	
	}
   
  
}
