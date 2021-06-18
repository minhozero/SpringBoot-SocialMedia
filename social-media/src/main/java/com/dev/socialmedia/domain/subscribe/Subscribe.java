package com.dev.socialmedia.domain.subscribe;

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

import com.dev.socialmedia.domain.user.User;

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
							name = "subscribe_uk",
							columnNames = {"fromUserId"	,"toUserId"} //두개이상
					)
			}
		)
public class Subscribe {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) //번호 증가 전략은 데이터베이스를 따라간다.
	private int id;
	
	@JoinColumn(name = "fromUserId")
	@ManyToOne
	private User fromUser; //구독 하는 사람
	
	@JoinColumn(name = "toUserId")
	@ManyToOne
	private User toUser; // 구독 받는 사람
	
	private LocalDateTime createDate;
	
	@PrePersist 
	public void createDate() {
		this.createDate = LocalDateTime.now();
	}
}
