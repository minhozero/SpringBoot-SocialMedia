package com.dev.socialmedia.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dev.socialmedia.config.auth.PrincipalDetails;
import com.dev.socialmedia.domain.image.Image;
import com.dev.socialmedia.domain.image.ImageRepository;
import com.dev.socialmedia.web.dto.image.ImageUploadDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ImageService {
	
	private final ImageRepository imageRepository;
	
	@Transactional(readOnly = true) // 영속석 컨택스트 변경 감지를 해서, 더티채킹. flush(반영)을 하지만 readOnly하면 X
	public Page<Image> 이미지스토리(int principalId, Pageable pageable){
		Page<Image> images = imageRepository.mStory(principalId, pageable);
		
		// images에 좋아요 상태 담기
		images.forEach((image)->{
			
			image.setLikeCount(image.getLikes().size());
			
			image.getLikes().forEach((like)->{
				if(like.getUser().getId() == principalId) { // 해당 이미지에 좋아요한 사람을 찾아서. 현재 로그인한 사람이 좋아요 한것인지 비교.
					image.setLikeState(true);
				}
			});
		});
		
		return images;
	}
	
	@Value("${file.path}") //application.yml
	private String uploadFolder;
	
	@Transactional
	public void 사진업로드(ImageUploadDto imageUploadDto, PrincipalDetails principalDetails) {
		UUID uuid = UUID.randomUUID(); // UUID
		String imageFileName = uuid+"_"+imageUploadDto.getFile().getOriginalFilename(); // 1.jpg
		System.out.println("이미지 파일이름 : " + imageFileName);
		
		Path imageFilePath = Paths.get(uploadFolder+imageFileName);
		
		// 통신. I/O -> 예외가 발생할 수 있다.
		try {
			Files.write(imageFilePath, imageUploadDto.getFile().getBytes());
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		// image 테이블에 저장.
		Image image = imageUploadDto.toEntity(imageFileName, principalDetails.getUser());
		imageRepository.save(image);
		//System.out.println(imageEntity.toString());
	}
	
	@Transactional(readOnly = true)
	public List<Image> 인기사진() {
		return imageRepository.mPopular();
	}
	
}
