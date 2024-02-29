package com.arun;

import com.arun.service.ExpenseCalculatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.ApplicationContext;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class MonthlyExpenseTrackerApplication {


	public static void main(String[] args) {

		ApplicationContext ctx=SpringApplication.run(MonthlyExpenseTrackerApplication.class, args);

		ExpenseCalculatorService service =ctx.getBean(ExpenseCalculatorService.class);
		service.calculateExpense();

	}

}
