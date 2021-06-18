package com.dev.socialmedia.domain.comment;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;

import com.dev.socialmedia.domain.image.Image;
import com.dev.socialmedia.domain.user.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder // signupDto 에서 builder 를 사용해 주기 위해 선언.
@AllArgsConstructor
@NoArgsConstructor
@Data 
@Entity // DB에 Table 생 성. 
public class Comment {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) //번호 증가 전략은 데이터베이스를 따라간다.
	private int id;
	
	@Column(length = 100 , nullable = false)
	private String content;
	
	@JsonIgnoreProperties({"images"})
	@JoinColumn(name = "userId")
	@ManyToOne(fetch = FetchType.EAGER)
	private User user;
	
	@JoinColumn(name = "imageId")
	@ManyToOne(fetch = FetchType.EAGER)
	private Image image;
	
	private LocalDateTime createDate;
	
	@PrePersist 
	public void createDate() {
		this.createDate = LocalDateTime.now();
	}
	
}
