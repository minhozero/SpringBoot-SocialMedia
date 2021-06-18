package com.dev.socialmedia.handler;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import com.dev.socialmedia.handler.ex.CustomApiException;
import com.dev.socialmedia.handler.ex.CustomException;
import com.dev.socialmedia.handler.ex.CustomValidationApiException;
import com.dev.socialmedia.handler.ex.CustomValidationException;
import com.dev.socialmedia.util.Script;
import com.dev.socialmedia.web.dto.CMRespDto;

@RestController
@ControllerAdvice 
public class ControllerExceptionHandler {
	
	@ExceptionHandler(CustomValidationException.class)
	public String validationException(CustomValidationException e) {
		// CMRespDto ,Script 비교
		// 1. 클라이언트에게 응답할 때 - Script 좋음
		// 2. Ajax통신 - CMRespDto 좋음
		// 3. Android 통신 - CMRespDto 좋음
		if(e.getErrorMap() == null) {
			return Script.back(e.getMessage());
		} else {
			return Script.back(e.getErrorMap().toString());
		}
	}
	
	@ExceptionHandler(CustomException.class)
	public String exception(CustomException e) {
		return Script.back(e.getMessage());
	}
	
	@ExceptionHandler(CustomValidationApiException.class)
	public ResponseEntity<?> validationApiException(CustomValidationApiException e) {
		return new ResponseEntity<>(new CMRespDto<>(-1, e.getMessage(), e.getErrorMap())  ,HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(CustomApiException.class)
	public ResponseEntity<?> apiException(CustomApiException e) {
		return new ResponseEntity<>(new CMRespDto<>(-1, e.getMessage(),null)  ,HttpStatus.BAD_REQUEST);
	}
	
}
