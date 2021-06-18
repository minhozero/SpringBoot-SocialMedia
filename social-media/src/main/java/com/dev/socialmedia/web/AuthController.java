package com.dev.socialmedia.web;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.dev.socialmedia.domain.user.User;
import com.dev.socialmedia.handler.ex.CustomValidationException;
import com.dev.socialmedia.service.AuthService;
import com.dev.socialmedia.web.dto.auth.SignupDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor // final 필드를 DI 할때 사용.
@Controller // 1. IoC 2. 파일을 리턴하는 컨트롤러
public class AuthController {
	
	private static final Logger log = LoggerFactory.getLogger(AuthController.class);
	
	private final AuthService authService;
	/* 의존성 주입 방법 
 		1. @Autowired 
		2. 아래 처럼.
	 * public AuthController(AuthService authService) { // 의존성 주입이 필요. 또는 @Autowired
	 * 선언 해주면 됨. this.authService = authService; }
	 *  3. @RequiredArgsConstructor
	 */
	
	@GetMapping("/auth/signin")
	public String signinForm() {
		return "auth/signin";
	}
	
	@GetMapping("/auth/signup")
	public String signupForm() {
		return "auth/signup";
	}
	
	// 회원가입 버튼 -> /auth/signup -> /auth/signin
	// 회원가입 버튼 X  시큐리티 CSFR 토큰 검사 -> securityConfig 에서 csrf disable 해줘야 정상적으로 호출됨. 
	@PostMapping("/auth/signup")
	public String signup(@Valid SignupDto signupDto, BindingResult bindingResult) { // key=value (x-www-form-urlencoded)
		// User <- SignupDto
		User user = signupDto.toEntity();
		User userEntity = authService.회원가입(user);
		System.out.println(userEntity);
		return "auth/signin";
	}
}
