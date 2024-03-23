package com.expenso.Iservice;

import java.util.List;
import java.util.Map;



public interface IExpenseCalculatorService {
	
	public  Map<String,Object> calculateExpense(String  month,List<String> inputFileData);
	
	

}
