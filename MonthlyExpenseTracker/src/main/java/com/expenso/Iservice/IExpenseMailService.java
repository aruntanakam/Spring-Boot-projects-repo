package com.expenso.Iservice;

import com.expenso.entity.EmailData;

public interface IExpenseMailService {
	
	public String sendMail(EmailData emailData);

}
