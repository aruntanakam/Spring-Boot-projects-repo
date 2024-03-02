package com.arun;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.ApplicationContext;

import com.arun.controller.ExpenseController;

import jakarta.mail.MessagingException;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class MonthlyExpenseTrackerApplication {


	public static void main(String[] args) throws MessagingException {

	
		ApplicationContext ctx=SpringApplication.run(MonthlyExpenseTrackerApplication.class, args);

		ExpenseController controller=ctx.getBean(ExpenseController.class);
		
		System.out.println(controller.getTotal());
		
		//System.out.println(controller.createExcelFile());
		
		System.out.println(controller.sendMail());
	}

}
