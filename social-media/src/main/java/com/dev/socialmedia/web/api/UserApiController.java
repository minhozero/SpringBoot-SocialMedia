package com.dev.socialmedia.web.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.dev.socialmedia.config.auth.PrincipalDetails;
import com.dev.socialmedia.domain.user.User;
import com.dev.socialmedia.handler.ex.CustomValidationApiException;
import com.dev.socialmedia.handler.ex.CustomValidationException;
import com.dev.socialmedia.service.SubscribeService;
import com.dev.socialmedia.service.UserService;
import com.dev.socialmedia.web.dto.CMRespDto;
import com.dev.socialmedia.web.dto.subscribe.SubscribeDto;
import com.dev.socialmedia.web.dto.user.UserUpdateDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class UserApiController {
	
	private final UserService userService;
	private final SubscribeService subscribeService;
	
	@PutMapping("/api/user/{principalId}/profileImageUrl")
	public ResponseEntity<?> profileImageurlUpdate(@PathVariable int principalId, MultipartFile profileImageFile, //profile.jsp 에서 form안의 file 의 name 값이랑 동일 해야 함. 
			@AuthenticationPrincipal PrincipalDetails principalDetails){ 
		User userEntity = userService.회원프로필사진변경(principalId, profileImageFile);
		principalDetails.setUser(userEntity);
		return new ResponseEntity<>(new CMRespDto<>(1, "프로필사진변경 성공", null), HttpStatus.OK );
	}
	
	@GetMapping("/api/user/{pageUserId}/subscribe")
	public ResponseEntity<?> subscribeList(@PathVariable int pageUserId, @AuthenticationPrincipal PrincipalDetails principalDetails){
		List<SubscribeDto> SubscribeDto = subscribeService.구독리스트(principalDetails.getUser().getId(), pageUserId);
		return new ResponseEntity<>(new CMRespDto<>(1, "구독자 정보 리스트 가져오기 성공", SubscribeDto) , HttpStatus.OK);
	}
	
	@PutMapping("/api/user/{id}")
	public CMRespDto<?> update(
			@PathVariable int id, 
			@Valid UserUpdateDto userUpdateDto, 
			BindingResult bindingResult, // 꼭 @Valid 가 적혀있는 파라미터 뒤에 적어줘야함.
			@AuthenticationPrincipal PrincipalDetails principalDetails) {

			User userEntity = userService.회원수정(id, userUpdateDto.toEntity());
			principalDetails.setUser(userEntity);
			System.out.println(userUpdateDto);
			return new CMRespDto<>(1, "회원수정완료", userEntity); // 응답시 userEntity의 모든 getter 함수가 호출되고 JSON으로 파싱하여 응답한다.
	}
}
