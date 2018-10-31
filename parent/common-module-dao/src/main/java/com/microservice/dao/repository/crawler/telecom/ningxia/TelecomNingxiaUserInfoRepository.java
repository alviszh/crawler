package com.microservice.dao.repository.crawler.telecom.ningxia;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.mobile.TaskMobile;
import com.microservice.dao.entity.crawler.telecom.ningxia.TelecomNingxiaUserInfo;

@Repository
public interface TelecomNingxiaUserInfoRepository extends JpaRepository<TelecomNingxiaUserInfo, Long>{
	
	TelecomNingxiaUserInfo findByTaskid(String taskid);
	
	@Transactional
	@Modifying
	@Query(value = "update TelecomNingxiaUserInfo set addr=?1 , cardid =?2 , name=?3 , operator =?4 , paperstype =?5 where taskid=?6")
	void updatewdzl(String addr, String cardid, String name, String operator, String paperstype, String taskid);

	@Transactional
	@Modifying
	@Query(value = "update TelecomNingxiaUserInfo set city=?1 , phone =?2 , accountstatus=?3 , netintime =?4 , netingtime =?5 , email =?6 where taskid=?7")
	void updatewdcp(String city, String phone, String accountstatus, String netintime, String day, String email,String taskid);

	@Transactional
	@Modifying
	@Query(value = "update TelecomNingxiaUserInfo set star=?1 where taskid=?2")
	void updatewdxj(String star, String taskid);

	@Transactional
	@Modifying
	@Query(value = "update TelecomNingxiaUserInfo set usableintegral=?1 where taskid=?2")
	void updatewdjf(String integral, String taskid);

	@Transactional
	@Modifying
	@Query(value = "update TelecomNingxiaUserInfo set remaining=?1 where taskid=?2")
	void updatewdyue(String remainMoney, String taskid);
	
}
 