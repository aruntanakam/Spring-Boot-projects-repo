package com.expenso.service;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.expenso.Iservice.IExpenseMailService;
import com.expenso.entity.EmailData;
import com.expenso.entity.InputData;
import com.expenso.entity.MonthAndYearInput;
import com.expenso.exception.MailFailedEception;

import freemarker.core.ParseException;
import freemarker.template.Configuration;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.TemplateException;
import freemarker.template.TemplateNotFoundException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ExpenseMailServiceImpl implements IExpenseMailService{
	
	@Autowired
	private JavaMailSender mailSender;
	
	@Value("${spring.mail.username}")
	private String fromAddress;
	
	@Autowired
	private ExpenseMiscService miscService;
	
	@Autowired
	private Configuration config;
	
	@Autowired
	private TemplateEngine engine;

	@Override
	public void sendMail(InputData data)  {
		// TODO Auto-generated method stub
		
		EmailData emailData=data.getMailData();
		MonthAndYearInput m=data.getMonthandyearinput();
		
		try {
			MimeMessage message =mailSender.createMimeMessage();
			MimeMessageHelper helper=new MimeMessageHelper(message,true);
			
			helper.setFrom(fromAddress);
			helper.setTo(emailData.getToAddress());
			helper.setCc(fromAddress);
			helper.setBcc("madhutanakam@gmail.com");
			helper.setSubject(miscService.getEmailSubject(m));
			//helper.setText(getFreeMarkerText(emailData,m),true);
			helper.setText(getThymeLeafTemplate(emailData,m),true);
			
			File f=new File(miscService.getOutputExcelFileName(m));
			
			if(!Files.exists(f.toPath()))
			{
			   log.error("unable to send mail as file does not exists");
			   throw new MailFailedEception("unable to send mail as output excel file does not exists");
			}
			
		   FileSystemResource fs=new FileSystemResource(f); 
		   
		   //FileSystemResource imgResource=new FileSystemResource(miscService.getClassPathResource("/static/images/expense_img.png"));
			
		   ClassPathResource imgResource=new ClassPathResource("/static/images/expense_img.png");
		   
			helper.addAttachment(f.getName(), fs);
			
			helper.addInline("expenseImg", imgResource);
			
			mailSender.send(message);
			
			log.info("mail sent successfully");
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error("mail sending failed {}",e.getMessage());
			throw new MailFailedEception();
		}
		
		
		
		
		
		
	}
	
	public String getFreeMarkerText(EmailData data,MonthAndYearInput m) throws Exception
	{
		Map<String,Object> map=new HashMap<>();
		
		map.put("userName", data.getUserName());
		map.put("month",m.getMonth());
		map.put("fromAddress", fromAddress);
		map.put("year", m.getYear());
		
		StringWriter out=new StringWriter();
     
		try {
			log.info("Reading freemarker template");
			config.getTemplate("freemarker_mail_template.ftlh").process(map, out);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error("error while reading freemarker template {}",e.getMessage());
			throw e;
		}
		
		return out.toString();
	}
	public String getThymeLeafTemplate(EmailData data,MonthAndYearInput m) throws Exception
	{
		try
		{
			log.info("Reading thymeleaf template");
			Context context=new Context();
			Map<String,Object> map=new HashMap<>();
					
					map.put("userName", data.getUserName());
					map.put("month",m.getMonth());
					map.put("fromAddress", fromAddress);
					map.put("year", m.getYear());
					
					context.setVariables(map);
					
					
		return engine.process("thymeleaf_mail_template.html", context);
		}
		catch(Exception e)
		{
			log.error("error while reading thymeleaf template:"+e.getMessage());
			throw e;
		}
	}

}
