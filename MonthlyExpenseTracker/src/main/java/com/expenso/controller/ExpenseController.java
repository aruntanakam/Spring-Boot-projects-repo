package com.expenso.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.expenso.Iservice.IExpenseCalculatorService;
import com.expenso.Iservice.IExpenseGeneratorService;
import com.expenso.Iservice.IExpenseMailService;
import com.expenso.Iservice.IFileReaderService;
import com.expenso.entity.EmailData;
import com.expenso.entity.InputData;
import com.expenso.entity.MonthAndYearInput;
import com.expenso.service.ExpenseMiscService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/expenso/api")
@Slf4j
public class ExpenseController {

	@Autowired
	private IExpenseCalculatorService calculatorService;

	@Autowired
	@Qualifier("excelService")
	private IExpenseGeneratorService excelService;

	@Autowired
	private ExpenseMiscService miscService;

	@Autowired
	private IExpenseMailService mailService;

	@Autowired
	private IFileReaderService readerService;

	@PostMapping("/send-mail")
	public ResponseEntity<String> sendMail(@RequestBody InputData data) {
		log.info("api request for sending mail  initiated");

		try {
			MonthAndYearInput m = data.getMonthandyearinput();
			EmailData emailData = data.getMailData();

			String inputFilePath = miscService.getInputFileName(m);

			List<String> list = readerService.readFile(inputFilePath);

			Map<String, Object> map = calculatorService.calculateExpense(m.getMonth(), list);

			excelService.generateFile(map, m);

			Thread.sleep(2000);

			mailService.sendMail(data);

			return new ResponseEntity<String>("Mail sent successfully", HttpStatus.OK);
		} catch (Exception e) {
			String errorMessage="Error occured in processing api,reason is :" + e.getMessage();
			log.error(errorMessage);
			return new ResponseEntity<String>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

}
