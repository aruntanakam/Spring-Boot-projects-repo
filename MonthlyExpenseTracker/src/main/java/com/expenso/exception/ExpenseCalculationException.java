package com.expenso.exception;

public class ExpenseCalculationException  extends RuntimeException{
	
	public ExpenseCalculationException() {
		super("Error occured while calculating expense");
	}

}
