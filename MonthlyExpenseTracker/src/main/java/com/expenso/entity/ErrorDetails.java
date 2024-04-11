package com.expenso.entity;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ErrorDetails {

	private LocalDateTime timeStamp;
	
	 private String status;
	 
	 private String errorMessage;
	 
	 
}
