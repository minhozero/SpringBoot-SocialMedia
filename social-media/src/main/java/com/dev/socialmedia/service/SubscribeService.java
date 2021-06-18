package com.dev.socialmedia.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.qlrm.mapper.JpaResultMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dev.socialmedia.domain.subscribe.SubscribeRepository;
import com.dev.socialmedia.handler.ex.CustomApiException;
import com.dev.socialmedia.web.dto.subscribe.SubscribeDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class SubscribeService {
	
	private final SubscribeRepository subscribeRepository;
	private final EntityManager em; // Repositoty EntityManager를 구현해서 만들어져 있는 구현체.
	
	@Transactional(readOnly = true)
	public List<SubscribeDto> 구독리스트(int principalId, int pageUserId) {
		
		// 쿼리 준비
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT u.id , u.username, u.profileImageUrl, ");
		sb.append("if ((SELECT 1 FROM subscribe where fromUserid = ? AND toUserId = u.id),1 , 0) subscribeState , ");
		sb.append("if ((?=u.id), 1, 0) equalUserState ");
		sb.append("FROM user u INNER JOIN subscribe s ");
		sb.append("ON u.id = s.toUserId ");
		sb.append("WHERE s.fromUserId = ? "); // ; 세미콜론은 필요 없음.
		
		// 쿼리 완성
		Query query = em.createNativeQuery(sb.toString())
				.setParameter(1, principalId)
				.setParameter(2, principalId)
				.setParameter(3, pageUserId);
		
		// 쿼리 실행 (qlrm 라이브러리 필요 = DTO에 DB결과를 매핑 하기 위해서)
		JpaResultMapper result = new JpaResultMapper();
		List<SubscribeDto> subscribesDtos =  result.list(query, SubscribeDto.class);
		
		return subscribesDtos;
	}
	
	@Transactional
	public void 구독(int fromUserId, int toUserId) {
		try {
			subscribeRepository.mSubscribe(fromUserId, toUserId);
		}catch (Exception e) {
			throw new CustomApiException("이미 구독을 하였습니다.");
		}
	}
	
	@Transactional
	public void 구독취소(int fromUserId, int toUserId) {
			subscribeRepository.mUnSubscribe(fromUserId, toUserId);
	}

}
