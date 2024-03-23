package com.expenso.exception;

public class InputFileReadException extends RuntimeException {
	
	public InputFileReadException()
	{
		super("Error in reading input file");
	}

}
