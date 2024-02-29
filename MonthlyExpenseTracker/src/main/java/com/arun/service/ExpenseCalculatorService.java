package com.arun.service;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

@Service
public class ExpenseCalculatorService {

	@Value("${expense.input.folder.name}")
	private String inputFolder;

	@Value("${expense.input.folder.name}")
	private String outputFolder;


	private static final  String[] MONTHS={"January","February","March","April","May","June","July","August","September","October","November","December"};


	public String getInputFileName()
	{


        return inputFolder +
				getMonth() + "_" + getYear() + ".txt";



	}

	public int getMonthValue()
	{
		LocalDate date=LocalDate.now();
		int temp=date.getMonthValue()-1;
        return ( temp== 0)?12:temp;
	}
	public String getMonth()
	{


		return (MONTHS[getMonthValue()-1]);

	}

	public String getYear()
	{

		int temp=LocalDate.now().getYear();

		int year=(getMonthValue()==12)?(temp-1):temp;

		return Integer.toString(year);
	}

	public  void calculateExpense()
	{
		try(BufferedReader br=new BufferedReader(new FileReader(getInputFileName()));) {



			String s=br.readLine();



			Map<String, List<String>> expensesNamesMap=new LinkedHashMap<String,List<String>>();
			Map<String, List<Double>> expensesCostMap=new LinkedHashMap<String,List<Double>>();


			while (s != null) {
				if (s.trim().startsWith(getMonth().substring(0, 3))) {
					ArrayList<String> names = new ArrayList<String>();
					ArrayList<Double> cost=new ArrayList<Double>();
					expensesNamesMap.put(s, names);
					expensesCostMap.put(s,cost);
					s=br.readLine();
					while ((s!= null )&& !((s=s.trim()).startsWith(getMonth().substring(0, 3)))) {
						s=s.trim();
						int index=s.indexOf(":");
						if(index>0)
						{
							String name=s.substring(0,index);
							Double c=Double.parseDouble(s.substring(index+1));
							names.add(name);
							cost.add(c);
						}
						s=br.readLine();
					}

				}
				else {
					s = br.readLine();
				}

			}

   double total=0;

 for(Map.Entry<String,List<Double>> item:expensesCostMap.entrySet())
		 {
			  List<Double> values=item.getValue();
			  System.out.println(item.getKey());
			  System.out.println(expensesNamesMap.get(item.getKey()));
			  System.out.println(values);

			  //total+=values.stream().mapToDouble(Double::doubleValue).sum();

			 total+=values.stream().reduce(0.0,Double::sum);

		 }
 System.out.println("Total expense for this month is:"+total);



			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
