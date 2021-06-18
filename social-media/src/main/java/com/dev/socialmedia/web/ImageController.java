package com.dev.socialmedia.web;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.dev.socialmedia.config.auth.PrincipalDetails;
import com.dev.socialmedia.domain.image.Image;
import com.dev.socialmedia.handler.ex.CustomValidationException;
import com.dev.socialmedia.service.ImageService;
import com.dev.socialmedia.web.dto.image.ImageUploadDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class ImageController {
	
	private final ImageService imageService;
	
	@GetMapping({"/","/image/story"})
	public String story() {
		return "image/story";
	}
	
	@GetMapping("/image/popular")
	public String popular(Model model) {
		List<Image> images = imageService.인기사진();
		model.addAttribute("images", images);
		return "image/popular";
	}

	@GetMapping("/image/upload")
	public String upload() {
		return "image/upload";
	}
	
	@PostMapping("/image")
	public String imageUpload(ImageUploadDto imageUploadDto, @AuthenticationPrincipal PrincipalDetails principalDetails) {
		// 얘는 어쩔 수 없음.
		if(imageUploadDto.getFile().isEmpty()) {
			throw new CustomValidationException("이미지가 첨부되지 않았습니다.", null);
		}
		// 서비스 호출
		imageService.사진업로드(imageUploadDto, principalDetails);
		return "redirect:/user/"+ principalDetails.getUser().getId();
	}
}
