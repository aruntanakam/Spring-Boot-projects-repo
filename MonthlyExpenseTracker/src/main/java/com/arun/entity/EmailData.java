package com.arun.entity;

public class EmailData {
	
	private String userName;
	
	private String[] toAddress;
	
	private String[] ccAddress;
	
	private String[] bccAddress;
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String[] getToAddress() {
		return toAddress;
	}

	public void setToAddress(String[] toAddress) {
		this.toAddress = toAddress;
	}

	public String[] getCcAddress() {
		return ccAddress;
	}

	public void setCcAddress(String[] ccAddress) {
		this.ccAddress = ccAddress;
	}

	public String[] getBccAddress() {
		return bccAddress;
	}

	public void setBccAddress(String[] bccAddress) {
		this.bccAddress = bccAddress;
	}

	
	
	

}
