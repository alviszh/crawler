package com.microservice.dao.repository.crawler.telecom.fujian;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.telecom.fujian.TelecomFujianUserInfo;

@Repository
public interface TelecomFujianUserInfoRepository extends JpaRepository<TelecomFujianUserInfo, Long>{
	
	TelecomFujianUserInfo findByTaskid(String taskid);
	
	@Transactional
	@Modifying
	@Query(value = "update TelecomFujianUserInfo set commonremaining=?1 , phone =?2 where taskid=?3")
	void updatewdyue(String remainMoney, String phone ,String taskid);

	@Transactional
	@Modifying
	@Query(value = "update TelecomFujianUserInfo set useintegral=?1 , expireintegral =?2, name =?3 , accountstatus =?4 where taskid=?5")
	void updatewdjifen(String useintegral,String expireintegral,String name, String accountstatus, String taskid);

	@Transactional
	@Modifying
	@Query(value = "update TelecomFujianUserInfo set name=?1 where taskid=?2")
	void updatewdname(String name, String taskid);

	
	@Transactional
	@Modifying
	@Query(value = "update TelecomFujianUserInfo set proname=?1 , accountstatus =?2 where taskid=?3")
	void updatewdStatus(String proname, String accountstatus, String taskid);

	
	@Transactional
	@Modifying
	@Query(value = "update TelecomFujianUserInfo set city=?1 where taskid=?2")
	void updatewdcity(String city, String taskid);

	
	
	
	
	

	

	
	
}
 