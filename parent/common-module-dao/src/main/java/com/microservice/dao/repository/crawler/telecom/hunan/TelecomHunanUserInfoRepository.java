package com.microservice.dao.repository.crawler.telecom.hunan;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.telecom.hunan.TelecomHunanUserInfo;
import com.microservice.dao.entity.crawler.telecom.neimenggu.TelecomNeimengguUserInfo;

@Repository
public interface TelecomHunanUserInfoRepository extends JpaRepository<TelecomHunanUserInfo, Long>{
	
	TelecomHunanUserInfo findByTaskid(String taskid);
	
	@Transactional
	@Modifying
	@Query(value = "update TelecomHunanUserInfo set commonremaining=?1 , phone =?2 where taskid=?3")
	void updatewdyue(String remainMoney, String phone ,String taskid);

	
	@Transactional
	@Modifying
	@Query(value = "update TelecomHunanUserInfo set expireintegral=?1 , useintegral =?2 , doubleintegral=?3 , salesintegral =?4 , historyintegral=?5  where taskid=?6")
	void updatewdjifen(String expireintegral, String useintegral, String doubleintegral, String salesintegral,
			String historyintegral, String taskid);

	@Transactional
	@Modifying
	@Query(value = "update TelecomHunanUserInfo set name=?1 where taskid=?2")
	void updatewdname(String name, String taskid);

	
	@Transactional
	@Modifying
	@Query(value = "update TelecomHunanUserInfo set proname=?1 , accountstatus =?2 where taskid=?3")
	void updatewdStatus(String proname, String accountstatus, String taskid);
	
	
	
	
	

	

	
	
}
 