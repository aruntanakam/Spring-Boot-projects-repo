package com.expenso.service;

import static com.expenso.constants.ExpenseConstants.EXPENSES_COST_MAP;
import static com.expenso.constants.ExpenseConstants.EXPENSES_NAMES_MAP;
import static com.expenso.constants.ExpenseConstants.EXPENSES_TOTAL_MAP_DAILY;
import static com.expenso.constants.ExpenseConstants.TOTAL_MAP;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import com.expenso.Iservice.IExpenseCalculatorService;
import com.expenso.entity.MonthAndYearInput;
import com.expenso.exception.ExpenseCalculationException;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ExpenseCalculatorServiceImpl implements IExpenseCalculatorService {

	@Autowired
	private ExpenseMiscService miscService;

	public Map<String, Object> calculateExpense(String  month,List<String> inputFileData)  {
		try
		{
			log.info("preparing to calculate expense");
			Map<String, List<String>> expensesNamesMap = new LinkedHashMap<String, List<String>>();
			Map<String, List<Double>> expensesCostMap = new LinkedHashMap<String, List<Double>>();
			Map<String, Double> expensesTotalMap = new LinkedHashMap<String, Double>();

			
			
			List<String> list = inputFileData;
			ListIterator<String> ite = list.listIterator();

			while (ite.hasNext()) {
				String str = ite.next();

				if (str != null && (str = str.trim()).startsWith(month.substring(0, 3))) {
					ArrayList<String> names = new ArrayList<String>();
					ArrayList<Double> cost = new ArrayList<Double>();
					expensesNamesMap.put(str, names);
					expensesCostMap.put(str, cost);

					String expense = "";

					while (ite.hasNext() && (expense = ite.next()) != null
							&& !(expense = expense.trim()).startsWith(month.substring(0, 3))) {

						expense = expense.trim();
						int index = expense.indexOf(":");
						if (index > 0) {

							names.add(expense.substring(0, index).trim());
							cost.add(Double.valueOf(expense.substring(index + 1).strip()));
						}
					}

					if ((expense = expense.trim()).startsWith(month.substring(0, 3))) {
						ite.previous();
					}

				}

			}

			double total = 0;

			for (Map.Entry<String, List<Double>> item : expensesCostMap.entrySet()) {
				List<Double> values = item.getValue();

				// total+=values.stream().mapToDouble(Double::doubleValue).sum();
				if (CollectionUtils.isNotEmpty(values)) {
					double temp = values.stream().reduce(0.0, Double::sum);

					expensesTotalMap.put(item.getKey(), temp);

					total += temp;
				}

			}
			log.info("Total expense for the month {} is {}:",month,total);

			Map<String, Object> result = new LinkedHashMap<String, Object>();

			result.put(EXPENSES_NAMES_MAP, expensesNamesMap);

			result.put(EXPENSES_COST_MAP, expensesCostMap);

			result.put(TOTAL_MAP, total);

			result.put(EXPENSES_TOTAL_MAP_DAILY, expensesTotalMap);

			return result;
		

		}
		catch(Exception e)
		{
			log.error("Exception occured while calculating expense {}",e.getMessage());
			throw new ExpenseCalculationException();
		}
}
}
