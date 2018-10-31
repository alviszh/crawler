package com.microservice.dao.repository.crawler.telecom.xinjiang;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.telecom.xinjiang.TelecomXinjiangUserInfo;

@Repository
public interface TelecomXinjiangUserInfoRepository extends JpaRepository<TelecomXinjiangUserInfo, Long>{
	TelecomXinjiangUserInfo findTopByTaskid(String taskId);
	
	@Transactional
	@Modifying
	@Query(value = "update TelecomXinjiangUserInfo set useable_point=?2 , invalid_point =?3 where taskid=?1")
	void updateXinjiangUserInfo(String taskId, String useablePoint ,String invalidPoint);
}
 