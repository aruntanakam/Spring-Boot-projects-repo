package com.expenso.Iservice;

import java.util.Map;

import com.expenso.entity.MonthAndYearInput;

public interface IExpenseGeneratorService {
	
	public void  generateFile(Map<String,Object> map,MonthAndYearInput m);
	

}
