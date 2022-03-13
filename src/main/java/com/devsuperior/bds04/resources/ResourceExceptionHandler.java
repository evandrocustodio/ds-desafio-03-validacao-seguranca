package com.devsuperior.bds04.resources;

import java.time.Instant;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.devsuperior.bds04.services.exceptions.DatabaseException;
import com.devsuperior.bds04.services.exceptions.ResourceNotFoundException;

@ControllerAdvice
public class ResourceExceptionHandler {
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<StandardError> entityNotFound(ResourceNotFoundException e, HttpServletRequest request) {

		StandardError error = new StandardError();
		error.setTimestamp(Instant.now());
		error.setStatus(HttpStatus.NOT_FOUND.value());
		error.setError("Resource Not Found");
		error.setMessage(e.getMessage());
		error.setPath(request.getRequestURI());

		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
	}

	@ExceptionHandler(DatabaseException.class)
	public ResponseEntity<StandardError> entityNotFound(DatabaseException e, HttpServletRequest request) {

		StandardError error = new StandardError();
		error.setTimestamp(Instant.now());
		error.setStatus(HttpStatus.BAD_REQUEST.value());
		error.setError("Databse Integrity Violation");
		error.setMessage(e.getMessage());
		error.setPath(request.getRequestURI());

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ValidationError> methodArgumentNotValidException(MethodArgumentNotValidException e,
			HttpServletRequest request) {

		ValidationError error = new ValidationError();
		error.setTimestamp(Instant.now());
		error.setStatus(HttpStatus.UNPROCESSABLE_ENTITY.value());
		error.setError("Erro de Validação");	
		e.getBindingResult().getFieldErrors().forEach(fr -> {
			error.addErrors(fr.getField(), fr.getDefaultMessage());
		});

		return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(error);
	}

}
