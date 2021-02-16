package com.money.api.exceptionHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class MoneyExceptionHandler extends ResponseEntityExceptionHandler {
	
	@Autowired
	private MessageSource messageSource;
	
	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		
		String userMessage = messageSource.getMessage("invalid.message", null, LocaleContextHolder.getLocale());
		String developerMessage = ex.getCause().toString();
		
		List<Error> errors = Arrays.asList(new Error(userMessage, developerMessage));
				
		return handleExceptionInternal(ex, errors, headers, HttpStatus.BAD_REQUEST, request);
	}
	
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		
		List<Error> errors = listErrors(ex.getBindingResult());
	
		return handleExceptionInternal(ex, errors, headers, HttpStatus.BAD_REQUEST, request);
	}
	
	private List<Error> listErrors(BindingResult bindingResult) {
		
		List<Error> errors = new ArrayList<>();
		
		for (FieldError fieldError : bindingResult.getFieldErrors()) {

			errors.add(new Error(
					messageSource.getMessage(fieldError, LocaleContextHolder.getLocale()), 
					fieldError.toString()));
		}
		
		return errors;		
	}
	
	public static class Error {
		
		private String userMessage;
		private String developerMessage;		
		
		public Error(String userMessage, String developerMessage) {
			this.userMessage = userMessage;
			this.developerMessage = developerMessage;
		}
		
		public String getUserMessage() {
			return userMessage;
		}
		
		public String getDeveloperMessage() {
			return developerMessage;
		}
	}
	
	@ExceptionHandler({EmptyResultDataAccessException.class})
	public ResponseEntity<Object> handleEmptyResultDataAccessException(EmptyResultDataAccessException ex, WebRequest request) {
		
		String userMessage = messageSource.getMessage("not-found.resource", null, LocaleContextHolder.getLocale());
		String developerMessage = ex.toString();
		
		List<Error> errors = Arrays.asList(new Error(userMessage, developerMessage));
		
		return handleExceptionInternal(ex, errors, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
	}
}
