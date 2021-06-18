package com.dev.socialmedia.config;



import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.dev.socialmedia.config.oauth.OAuth2DetailsService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@EnableWebSecurity // 해당 파일로 시큐리티를 활성화 
@Configuration // IoC 
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	private final OAuth2DetailsService oAuth2DetailsService;
	
	@Bean
	public BCryptPasswordEncoder encode() {
		return new BCryptPasswordEncoder();
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// super 삭제 - 기존 시큐리티가 가지고 있는 기능 비활성화
		http.csrf().disable(); //이걸 써야 시큐리티 CSFR 토큰 검사 안함. 
		http.authorizeRequests()		
			.antMatchers("/", "/user/**", "/image/**", "/subscribe/**", "/comment/**", "/api/**").authenticated() // 해당 url은 인증 필요 (403 오류)
			.anyRequest().permitAll() // 나머지는 허용
			.and()
			.formLogin()
			.loginPage("/auth/signin") //모든 url 호출시 로그인 페이지로 이동. GET
			.loginProcessingUrl("/auth/signin") // POST -> 스프링 시큐리티가 낚아채서. 로그인 프로세스 진행. (프론트 - > 스프링 시큐리티)
			.defaultSuccessUrl("/")// 정상 로그인 되면 / 이동
//			.successHandler(new AuthenticationSuccessHandler() {
//				@Override
//				public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
//						Authentication authentication) throws IOException, ServletException {
//					
//				}
//			})
			.and()
			.oauth2Login() // form 로그인도 하고, oauth2로그인도 함.
			.userInfoEndpoint() // oauth2 로그인을 하면 최종응답을 회원정보 바로 받을 수 있다.
			.userService(oAuth2DetailsService); // 
	}
}
