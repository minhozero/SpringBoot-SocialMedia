package com.dev.socialmedia.config.auth;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.dev.socialmedia.domain.user.User;
import com.dev.socialmedia.domain.user.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PrincipalDetailsService implements UserDetailsService {

	private final UserRepository userRepository;
	
	// 1. 패스워드는 스프링 시큐리티가 체크함.
	// 2. 리턴이 잘 되면 자동으로 UserDetails 타입을 세션을 만듬.
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User userEntity = userRepository.findByUsername(username);
		
		if(userEntity==null) {
			return null;
		} else {
			return new PrincipalDetails(userEntity);
		}
	}

}
