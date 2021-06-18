package com.dev.socialmedia.handler.aop;

import java.util.HashMap;
import java.util.Map;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import com.dev.socialmedia.handler.ex.CustomValidationApiException;
import com.dev.socialmedia.handler.ex.CustomValidationException;

@Component // RestController, Service 등 Component를 상속해서 만들어짐.
@Aspect // AOP 처리를 위한 어노테이션.
public class ValidationAdvice {
	
	@Around(value = "execution(* com.dev.socialmedia.web.api.*Controller.*(..))")
	public Object apiAdvice(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
//		System.out.println("web api 컨트롤러========================");
		Object[] args = proceedingJoinPoint.getArgs();
		for(Object arg : args) {
			if(arg instanceof BindingResult) {
//				System.out.println("유효성 검사를 하는 함수입니다.");
				BindingResult bindingResult = (BindingResult) arg;
				
				if(bindingResult.hasErrors()) {
//					System.out.println("나 실행 됨???==========================");
					Map<String,String> errorMap = new HashMap<>();
					for(FieldError error : bindingResult.getFieldErrors()) {
						errorMap.put(error.getField(), error.getDefaultMessage());
					}
					throw new CustomValidationApiException("유효성 검사 실패", errorMap);
				}
			}
			System.out.println(arg);
		}
		// proceedingJoinPoint => profile 함수의 모든 곳에 접근할 수 있는 변수
		// profile 함수보다 먼저 실행
		
		return proceedingJoinPoint.proceed(); // profile 함수가 실행됨.
	}
	
	@Around(value = "execution(* com.dev.socialmedia.web.*Controller.*(..))")
	public Object advice(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
//		System.out.println("web 컨트롤러========================");
		Object[] args = proceedingJoinPoint.getArgs();
		for(Object arg : args) {
			if(arg instanceof BindingResult) {
				BindingResult bindingResult = (BindingResult) arg;
//				System.out.println("유효성 검사를 하는 함수입니다.");
				if(bindingResult.hasErrors()) {
					Map<String,String> errorMap = new HashMap<>();
					for(FieldError error : bindingResult.getFieldErrors()) {
						errorMap.put(error.getField(), error.getDefaultMessage());
					}
					throw new CustomValidationException("유효성 검사 실패", errorMap);
				}
			}
		}
		return proceedingJoinPoint.proceed();
	}
}


