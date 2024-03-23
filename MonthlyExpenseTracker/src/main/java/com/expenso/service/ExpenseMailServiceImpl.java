package com.expenso.service;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URI;
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

import freemarker.core.ParseException;
import freemarker.template.Configuration;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.TemplateException;
import freemarker.template.TemplateNotFoundException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
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
	public String sendMail(EmailData emailData) {
		// TODO Auto-generated method stub
		
		
		
		try {
			MimeMessage message =mailSender.createMimeMessage();
			MimeMessageHelper helper=new MimeMessageHelper(message,true);
			
			helper.setFrom(fromAddress);
			helper.setTo(emailData.getToAddress());
			helper.setCc(emailData.getCcAddress());
			helper.setBcc(emailData.getBccAddress());
			helper.setSubject(miscService.getEmailSubject());
			//helper.setText(getFreeMarkerText(emailData),true);
			helper.setText(getThymeLeafTemplate(emailData),true);
			
			File f=new File(miscService.getOutputExcelFileName());
			
			if(!Files.exists(f.toPath()))
			{
			   return "unable to send mail as file does not exists";	
			}
			
		   FileSystemResource fs=new FileSystemResource(f); 
		   
		   //FileSystemResource imgResource=new FileSystemResource(miscService.getClassPathResource("/static/images/expense_img.png"));
			
		   ClassPathResource imgResource=new ClassPathResource("/static/images/expense_img.png");
		   
			helper.addAttachment(f.getName(), fs);
			
			helper.addInline("expenseImg", imgResource);
			
			mailSender.send(message);
			
			return "mail sent successfully";
			
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "mail sending failed";
		}
		
		
		
		
		
		
	}
	
	public String getFreeMarkerText(EmailData data)
	{
		Map<String,Object> map=new HashMap<>();
		
		map.put("userName", data.getUserName());
		map.put("month",miscService.getMonth());
		map.put("fromAddress", fromAddress);
		map.put("year", miscService.getYear());
		
		StringWriter out=new StringWriter();
     
		try {
			config.getTemplate("freemarker_mail_template.ftlh").process(map, out);
		} catch (TemplateNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedTemplateNameException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TemplateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return out.toString();
	}
	public String getThymeLeafTemplate(EmailData data)
	{
		Context context=new Context();
Map<String,Object> map=new HashMap<>();
		
		map.put("userName", data.getUserName());
		map.put("month",miscService.getMonth());
		map.put("fromAddress", fromAddress);
		map.put("year", miscService.getYear());
		
		context.setVariables(map);
		
		
		 return engine.process("thymeleaf_mail_template.html", context);
	}

}
