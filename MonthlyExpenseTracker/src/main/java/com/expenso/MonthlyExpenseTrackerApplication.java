package com.expenso;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.PropertySource;

import com.expenso.controller.ExpenseController;

import jakarta.mail.MessagingException;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@PropertySource("classpath:/static/FolderPaths.properties")
public class MonthlyExpenseTrackerApplication {


	public static void main(String[] args) throws MessagingException {

	
		SpringApplication.run(MonthlyExpenseTrackerApplication.class, args);

		
		
		
	}

}
