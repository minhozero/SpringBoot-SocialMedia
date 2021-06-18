package com.dev.socialmedia.domain.user;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;

import com.dev.socialmedia.domain.image.Image;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// JPA - Java Persistencs API (자바로 데이터를 영구적으로 저장할 수 있는 API) = DB

@Builder // signupDto 에서 builder 를 사용해 주기 위해 선언.
@AllArgsConstructor
@NoArgsConstructor
@Data 
@Entity // DB에 Table 생 성. 
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) //번호 증가 전략은 데이터베이스를 따라간다.
	private int id;
	
	@Column(length=100, unique = true) // OAuth2 로그인을 위해 컬럼 늘리기.
	private String username;
	@Column(nullable = false)
	private String password;
	@Column(nullable = false)
	private String name;
	private String website; // 웹 사이트
	private String bio; // 자기소개
	@Column(nullable = false)
	private String email;
	private String phone;
	private String gender;	
	
	private String profileImageUrl; // 사진
	private String role; // 권한
	
	// 나는 연관관계의 주인이 아님. 그러므로 테이블에 컬럼을 만들지 않는다.
	// User를 select 할 때 해당 User id 로 등록된 image를 다 가져와라.
	// Lazy = User를 Select할 때 해당 User id로 등록된 image들을 가져오지마. - 대신 getImages() 함수 호출될 때 가져와.
	// Eager = User를 select 할때 해당 User id로 등록된 image들을 전부 Join해서 가져와.
	@OneToMany(mappedBy =  "user", fetch = FetchType.LAZY) 
	@JsonIgnoreProperties({"user"}) // image의 user getter 호출 안하도록 설정. // JPA 무한참조를 막기 위해 씀.
	private List<Image> images; // 양방향 매핑.
	
	private LocalDateTime createDate;
	
	@PrePersist // 디비에 INSERT 되기 직전에 실행
	public void createDate() {
		this.createDate = LocalDateTime.now();
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", password=" + password + ", name=" + name + ", website="
				+ website + ", bio=" + bio + ", email=" + email + ", phone=" + phone + ", gender=" + gender
				+ ", profileImageUrl=" + profileImageUrl + ", role=" + role +", createDate=" + createDate + "]";
	}
}
