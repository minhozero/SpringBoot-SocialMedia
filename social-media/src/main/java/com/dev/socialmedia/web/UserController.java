package com.dev.socialmedia.web;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.dev.socialmedia.config.auth.PrincipalDetails;
import com.dev.socialmedia.domain.user.User;
import com.dev.socialmedia.service.UserService;
import com.dev.socialmedia.web.dto.user.UserProfileDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class UserController {
	
	private final UserService userService;
	
	@GetMapping("/user/{pageUserId}")
	public String profile(@PathVariable int pageUserId, Model model, @AuthenticationPrincipal PrincipalDetails principalDetails) {
		UserProfileDto dto = userService.회원프로필(pageUserId, principalDetails.getUser().getId());
		model.addAttribute("dto", dto);
		return "user/profile";
	}
	
	@GetMapping("/user/{id}/update")
	public String update(@PathVariable int id, @AuthenticationPrincipal PrincipalDetails principalDetails) {
		//System.out.println("세션정보1 : " + principalDetails.getUser());
		
		/* 세션찾기 비추천.
		 * Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		 * PrincipalDetails mPrincipalDetails = (PrincipalDetails) auth.getPrincipal();
		 * System.out.println("세션정보2 : " + mPrincipalDetails.getUser());
		 */
		
		return "user/update";
	}
	
}
