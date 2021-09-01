package com.cognixia.jump.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@ControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<?> resourceNotFound(ResourceNotFoundException ex, WebRequest request) {
		
		// what data will be returned back in response when Exception is thrown
		ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(), request.getDescription(false));
		
		// constructing the response
		return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
	}
	
	// Need to add in exception handler for UserLoginFailedException!
	@ExceptionHandler(UserLoginFailedException.class)
	public ResponseEntity<?> resourceNotFound(UserLoginFailedException ex, WebRequest request) {
		
		// what data will be returned back in response when Exception is thrown
		ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(), request.getDescription(false));
		
		// constructing the response
		return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
	}
}
