package com.dev.socialmedia.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dev.socialmedia.domain.comment.Comment;
import com.dev.socialmedia.domain.comment.CommentRepository;
import com.dev.socialmedia.domain.image.Image;
import com.dev.socialmedia.domain.user.User;
import com.dev.socialmedia.domain.user.UserRepository;
import com.dev.socialmedia.handler.ex.CustomApiException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CommentService {
	
	private final CommentRepository commentRepository;
	private final UserRepository userRepository;
	
	@Transactional
	public Comment 댓글쓰기(String content, int imageId, int userId) {
		
		// tip (객체를 만들때 id 값만 담아서 insert 할 수 있다.)
		// 대신 return 될때 image, user 객체는 id값만 가지고 있는 객체를 리턴 받는다. (나머지는 null)
		Image image = new Image();
		image.setId(imageId);
		
		User userEntity = userRepository.findById(userId).orElseThrow(()->{
			throw new CustomApiException("유저 아이디를 찾을 수 없습니다.");
		});
		
		Comment comment = new Comment();
		comment.setContent(content);
		comment.setImage(image);
		comment.setUser(userEntity);
		
		return commentRepository.save(comment);
	}
	@Transactional
	public void 댓글삭제(int id) {
		try {
			commentRepository.deleteById(id);
		}catch (Exception e) {
			throw new CustomApiException(e.getMessage());
		}
	}
}
