package com.expenso.controller;

import static com.expenso.constants.ExpenseConstants.TOTAL_MAP;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RestController;

import com.expenso.Iservice.IExpenseCalculatorService;
import com.expenso.Iservice.IExpenseGeneratorService;
import com.expenso.Iservice.IExpenseMailService;
import com.expenso.entity.EmailData;
import com.expenso.service.ExpenseMiscService;

@RestController
public class ExpenseController {
	
	
	@Autowired
	private IExpenseCalculatorService calculatorService;
	
	@Autowired
	@Qualifier("excelService")
	private IExpenseGeneratorService excelService;
	
	@Autowired
	private ExpenseMiscService miscService;
	
	@Autowired
	private IExpenseMailService mailService;
	
	
	
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
   
   
   public String sendMail()
   {
	  EmailData data=new EmailData();  
	  
	 
	  
	  data.setToAddress(new String[] {"aruntanakam2000@gmail.com"});
	  
	  data.setUserName("Arunkumar Tanakam");
	  
	  data.setCcAddress(new String[] {"aruntanakam2000@gmail.com"});
	  
	  data.setBccAddress(new String[] {"aruntanakam0107@gmail.com","madhutanakam@gmail.com"});
	  
	  return mailService.sendMail(data);
	  
   }
   
  
}
