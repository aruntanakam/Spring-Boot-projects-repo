package com.expenso.exception;

public class MailFailedEception extends RuntimeException {
	
	public MailFailedEception(String message) {
		super(message);
	}
	
	public MailFailedEception()
	{
		super("Error occured while sending mail");
	}

}
