package com.dev.socialmedia.domain.likes;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

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
@Table(
			uniqueConstraints = {
					@UniqueConstraint(
							name = "likes_uk",
							columnNames = {"imageId"	,"userId"} //두개이상
					)
			}
		)
public class Likes {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) //번호 증가 전략은 데이터베이스를 따라간다.
	private int id;
	
	@JoinColumn(name = "imageId")
	@ManyToOne
	private Image image; // 1개의 이미지에 N개의 좋아요 가능.
	
	
	@JsonIgnoreProperties({"images"}) // 양방향 참조할때 무한 참조 조심.
	@JoinColumn(name = "userId")
	@ManyToOne
	private User user; // 1명의 유저는 N개의 좋아요 가능
	
	private LocalDateTime createDate;
	
	@PrePersist 
	public void createDate() {
		this.createDate = LocalDateTime.now();
	}
}
