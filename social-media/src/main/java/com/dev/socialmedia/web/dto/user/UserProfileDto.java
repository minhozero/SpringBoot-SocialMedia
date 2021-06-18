package com.dev.socialmedia.web.dto.user;

import com.dev.socialmedia.domain.user.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserProfileDto {
		private boolean pageOwnerState; // 
		private int imageCount; // 게시물 개수
		private boolean subscribeState; //구독 상태
		private int subscribeCount; // 구독한 유저 수.
		private User user;
}
