package com.dev.socialmedia.web.dto.user;

import javax.validation.constraints.NotBlank;

import com.dev.socialmedia.domain.user.User;

import lombok.Data;

@Data
public class UserUpdateDto {
	@NotBlank
	private String name; // 필수값
	@NotBlank
	private String password; // 필수값
	private String website;
	private String bio;
	private String phone;
	private String gender;
	
	// 코드수정 해야함.
	public User toEntity() {
		return User.builder()
				.name(name)
				.password(password)
				.website(website)
				.bio(bio)
				.phone(phone)
				.gender(gender)
				.build();
	}
}
