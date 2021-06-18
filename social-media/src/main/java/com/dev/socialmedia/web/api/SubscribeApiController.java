package com.dev.socialmedia.web.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dev.socialmedia.config.auth.PrincipalDetails;
import com.dev.socialmedia.service.SubscribeService;
import com.dev.socialmedia.web.dto.CMRespDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class SubscribeApiController {
	
	private final SubscribeService subscribeService;
	
	@PostMapping("/api/subscribe/{toUserId}")
	public ResponseEntity<?> subscribe(@AuthenticationPrincipal PrincipalDetails principalDetails, @PathVariable int toUserId){
		subscribeService.구독(principalDetails.getUser().getId(), toUserId);
		return new ResponseEntity<>(new CMRespDto<>(1, "구독 성공", "null"), HttpStatus.OK);
	}
	
	@DeleteMapping("/api/subscribe/{toUserId}")
	public ResponseEntity<?> unSubscribe(@AuthenticationPrincipal PrincipalDetails principalDetails, @PathVariable int toUserId){
		subscribeService.구독취소(principalDetails.getUser().getId(), toUserId);
		return new ResponseEntity<>(new CMRespDto<>(1, "구독취소 성공", "null"), HttpStatus.OK);
	}
	
}
