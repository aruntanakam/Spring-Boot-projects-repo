package com.expenso.exception;

public class InputFileReadException extends RuntimeException {
	
	public InputFileReadException()
	{
		super("Error in reading input file,it may not exist or may be blocked by other resources");
	}

}
