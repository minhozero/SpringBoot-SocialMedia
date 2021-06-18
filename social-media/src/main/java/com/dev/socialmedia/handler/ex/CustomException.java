package com.dev.socialmedia.handler.ex;

public class CustomException  extends RuntimeException{
	
	// 객체를 구분
	private static final long serialVersionUID = 1L;
	
	public CustomException(String message) {
		super(message);
	}
}
