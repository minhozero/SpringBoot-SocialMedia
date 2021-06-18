package com.dev.socialmedia.config.oauth;

import java.util.Map;
import java.util.UUID;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.dev.socialmedia.config.auth.PrincipalDetails;
import com.dev.socialmedia.domain.user.User;
import com.dev.socialmedia.domain.user.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class OAuth2DetailsService extends DefaultOAuth2UserService{
	
	private final UserRepository userRepository;
	
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		//System.out.println("OAuth2 서비스 웹~");
		OAuth2User oAuth2User = super.loadUser(userRequest);
		//System.out.println(oAuth2User.getAttributes());
		Map<String, Object> userInfo = oAuth2User.getAttributes();
		String username = "facebook_" + (String) userInfo.get("id");
		// 시큐리티 IOC 등록 되기 전에 oauth2가 먼저 등록 되면 시큐리티에서 BCryptPasswordEncoder를 못찾음. 오류발생되서 new 함.
		String password = new BCryptPasswordEncoder().encode(UUID.randomUUID().toString()); 
		String email = (String) userInfo.get("email");
		String name = (String) userInfo.get("name");
		
		User userEntity = userRepository.findByUsername(username); // 유저가 있는지 확인.
		
		if (userEntity == null) { // 페이스북 최초 로그인이면 저장.
			User user = User.builder()
					.username(username)
					.password(password)
					.email(email)
					.name(name)
					.role("ROLE_USER")
					.build();
			return new PrincipalDetails(userRepository.save(user) , oAuth2User.getAttributes());
		} else { // 이미 회원 가입이 되어 있을때.
			return new PrincipalDetails(userEntity, oAuth2User.getAttributes());
		}
	}
}
