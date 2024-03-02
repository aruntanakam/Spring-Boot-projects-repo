package com.arun.service;

import static com.arun.constants.ExpenseConstants.EXPENSES_COST_MAP;
import static com.arun.constants.ExpenseConstants.EXPENSES_NAMES_MAP;
import static com.arun.constants.ExpenseConstants.EXPENSES_TOTAL_MAP_DAILY;
import static com.arun.constants.ExpenseConstants.TOTAL_MAP;

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

import com.arun.Iservice.IExpenseCalculatorService;

@Service
@PropertySource("classpath:/static/FolderPaths.properties")
public class ExpenseCalculatorServiceImpl implements IExpenseCalculatorService {

	@Autowired
	private ExpenseMiscService miscService;

	public Map<String, Object> calculateExpense() {
		try {

			/*
			 * BufferedReader br = new BufferedReader(new
			 * FileReader(miscService.getInputFileName()));
			 */

			// String s = br.readLine();

			Map<String, List<String>> expensesNamesMap = new LinkedHashMap<String, List<String>>();
			Map<String, List<Double>> expensesCostMap = new LinkedHashMap<String, List<Double>>();
			Map<String, Double> expensesTotalMap = new LinkedHashMap<String, Double>();

			/*
			 * while (s != null) { if
			 * (s.trim().startsWith(miscService.getMonth().substring(0, 3))) {
			 * ArrayList<String> names = new ArrayList<String>(); ArrayList<Double> cost =
			 * new ArrayList<Double>(); expensesNamesMap.put(s, names);
			 * expensesCostMap.put(s, cost); s = br.readLine(); while ((s != null) && !((s =
			 * s.trim()).startsWith(miscService.getMonth().substring(0, 3)))) { s =
			 * s.trim(); int index = s.indexOf(":"); if (index > 0) { String name =
			 * s.substring(0, index); Double c = Double.parseDouble(s.substring(index + 1));
			 * names.add(name); cost.add(c); } s = br.readLine(); }
			 * 
			 * } else { s = br.readLine(); }
			 * 
			 * }
			 */

			List<String> list = Files.readAllLines(Paths.get(miscService.getInputFileName()));

			ListIterator<String> ite = list.listIterator();

			while (ite.hasNext()) {
				String str = ite.next();

				if (str != null && (str = str.trim()).startsWith(miscService.getMonth().substring(0, 3))) {
					ArrayList<String> names = new ArrayList<String>();
					ArrayList<Double> cost = new ArrayList<Double>();
					expensesNamesMap.put(str, names);
					expensesCostMap.put(str, cost);

					String expense = "";

					while (ite.hasNext() && (expense = ite.next()) != null
							&& !(expense = expense.trim()).startsWith(miscService.getMonth().substring(0, 3))) {

						expense = expense.trim();
						int index = expense.indexOf(":");
						if (index > 0) {

							names.add(expense.substring(0, index));
							cost.add(Double.parseDouble(expense.substring(index + 1)));
						}
					}
					
					if((expense = expense.trim()).startsWith(miscService.getMonth().substring(0, 3)))
					{
						ite.previous();
					}

				}

			}

			double total = 0;

			for (Map.Entry<String, List<Double>> item : expensesCostMap.entrySet()) {
				List<Double> values = item.getValue();
				/*
				 * System.out.println(item.getKey());
				 * System.out.println(expensesNamesMap.get(item.getKey()));
				 * System.out.println(values);
				 */

				// total+=values.stream().mapToDouble(Double::doubleValue).sum();
				if (CollectionUtils.isNotEmpty(values)) {
					double temp = values.stream().reduce(0.0, Double::sum);

					expensesTotalMap.put(item.getKey(), temp);

					total += temp;
				}

			}
			// System.out.println("Total expense for this month is:"+total);

			Map<String, Object> result = new LinkedHashMap<String, Object>();

			result.put(EXPENSES_NAMES_MAP, expensesNamesMap);

			result.put(EXPENSES_COST_MAP, expensesCostMap);

			result.put(TOTAL_MAP, total);

			result.put(EXPENSES_TOTAL_MAP_DAILY, expensesTotalMap);

			return result;

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

}
