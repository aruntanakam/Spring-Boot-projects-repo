package com.arun.Iservice;

import com.arun.entity.EmailData;

public interface IExpenseMailService {
	
	public String sendMail(EmailData emailData);

}
