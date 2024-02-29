package com.arun.service;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.springframework.stereotype.Service;

@Service
public class ExpenseCalculatorService {
	
	public static void calculateExpense()
	{
		try(BufferedReader br=new BufferedReader(new FileReader(""))) {
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
