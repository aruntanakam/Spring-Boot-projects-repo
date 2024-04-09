package com.expenso.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.expenso.Iservice.IExpenseCalculatorService;
import com.expenso.Iservice.IExpenseGeneratorService;
import com.expenso.Iservice.IExpenseMailService;
import com.expenso.Iservice.IFileReaderService;
import com.expenso.Iservice.ISchedulingService;
import com.expenso.entity.EmailData;
import com.expenso.entity.InputData;
import com.expenso.entity.MonthAndYearInput;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@EnableScheduling
public class SchedulingService implements ISchedulingService {

	@Autowired
	private IFileReaderService readerService;

	@Autowired
	private ExpenseMiscService miscService;

	@Autowired
	private IExpenseCalculatorService calculatorService;

	@Autowired
	@Qualifier("excelService")
	private IExpenseGeneratorService generatorService;

	@Autowired
	private IExpenseMailService mailService;

	@Scheduled(cron = "0 0 0/5 1 * ?")
	public void monthy_calculate() {

		log.info("Scheduling staarted for calculating monthly expense at {}", LocalDateTime.now());

		try {
			String month = miscService.getMonth();

			String year = miscService.getYear();

			MonthAndYearInput m_input = new MonthAndYearInput();

			m_input.setMonth(month);
			m_input.setYear(year);

			String inputFileName = miscService.getInputFileName(m_input);

			List<String> list = readerService.readFile(inputFileName);

			Map<String, Object> map = calculatorService.calculateExpense(month, list);

			generatorService.generateFile(map, m_input);

			Thread.sleep(2000);

			EmailData mailData = new EmailData();

			mailData.setToAddress("aruntanakam2000@gmail.com");
			mailData.setUserName("Arunkumar");

			InputData data = new InputData();
			data.setMailData(mailData);
			data.setMonthandyearinput(m_input);

			mailService.sendMail(data);

		} catch (Exception e) {
			log.error("Error occured in scheduling service:{}", e.getMessage());
		}

	}

}
