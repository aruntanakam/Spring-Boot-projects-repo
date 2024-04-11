package com.expenso.controller;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.expenso.entity.ErrorDetails;
import com.expenso.exception.ExpenseCalculationException;
import com.expenso.exception.InputFileReadException;
import com.expenso.exception.MailFailedEception;
import com.expenso.exception.OutputFileGenerationException;

@RestControllerAdvice
public class ExceptionController {

	public ErrorDetails getErrorDetails(HttpStatus status, String message) {
		return ErrorDetails.builder().timeStamp(LocalDateTime.now()).status(status.getReasonPhrase())
				.errorMessage(message).build();
	}

	@ExceptionHandler(InputFileReadException.class)
	public ResponseEntity<ErrorDetails> error(InputFileReadException e) {
		HttpStatus status = HttpStatus.NOT_FOUND;

		ErrorDetails details = getErrorDetails(status, e.getMessage());
		return new ResponseEntity<ErrorDetails>(details, status);
	}

	@ExceptionHandler(ExpenseCalculationException.class)
	public ResponseEntity<ErrorDetails> error(ExpenseCalculationException e) {

		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

		ErrorDetails details = getErrorDetails(status, e.getMessage());
		return new ResponseEntity<ErrorDetails>(details, status);
	}

	@ExceptionHandler(MailFailedEception.class)
	public ResponseEntity<ErrorDetails> error(MailFailedEception e) {

		HttpStatus status = (e.getMessage().contains("excel")) ? HttpStatus.NOT_FOUND
				: HttpStatus.INTERNAL_SERVER_ERROR;

		ErrorDetails details = getErrorDetails(status, e.getMessage());
		return new ResponseEntity<ErrorDetails>(details, status);
	}

	@ExceptionHandler(OutputFileGenerationException.class)
	public ResponseEntity<ErrorDetails> error(OutputFileGenerationException e) {

		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

		ErrorDetails details = getErrorDetails(status, e.getMessage());
		return new ResponseEntity<ErrorDetails>(details, status);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorDetails> error(Exception e) {

		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

		ErrorDetails details = getErrorDetails(status, e.getMessage());
		return new ResponseEntity<ErrorDetails>(details, status);
	}

}
