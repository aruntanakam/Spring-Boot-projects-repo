package com.expenso.exception;

public class OutputFileGenerationException extends RuntimeException {
	
	public OutputFileGenerationException()
	{
		super("Error occured in generating output file");
	}

}
