package com.dev.socialmedia.domain.image;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.PrePersist;
import javax.persistence.Transient;

import com.dev.socialmedia.domain.comment.Comment;
import com.dev.socialmedia.domain.likes.Likes;
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
public class Image {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) //번호 증가 전략은 데이터베이스를 따라간다.
	private int id;
	private String caption; // 사진 넣을때 글자.
	private String postImageUrl; //사진을 전송 받아서 사진을 서버 특정 폴더에 저장. - DB에 저장 경로 저장(insert)
	
	@JsonIgnoreProperties({"images"}) // User의 images는 무시.
	@JoinColumn(name = "userId")
	@ManyToOne(fetch = FetchType.EAGER) // 이미지를 Select 하면 조인해서 User정보를 같이 들고 온다.
	private User user;
	
	// 이미지 좋아요.
	@JsonIgnoreProperties({"image"}) 
	@OneToMany(mappedBy = "image")
	private List<Likes> likes;
	
	// 댓글
	@OrderBy("id DESC")
	@JsonIgnoreProperties({"image"})  //comment 에서 image를 참조할 경우 무한참조에 걸리기 때문에 제외.
	@OneToMany(mappedBy = "image")
	private List<Comment> comments;
	
	private LocalDateTime createDate;
	
	@PrePersist 
	public void createDate() {
		this.createDate = LocalDateTime.now();
	}
	
	@Transient //DB에 컬럼이 만들어 지지 않는다.
	private boolean likeState;
	
	@Transient
	private int likeCount;
	
	// 오브젝트를 콘솔에 출력할 때 문제가 될 수 있어서 User 부분을 출력되지 않게 함.
//	@Override
//	public String toString() {
//		return "Image [id=" + id + ", caption=" + caption + ", postImageUrl=" + postImageUrl 
//				+ ", createDate=" + createDate + "]";
//	}
	
}
