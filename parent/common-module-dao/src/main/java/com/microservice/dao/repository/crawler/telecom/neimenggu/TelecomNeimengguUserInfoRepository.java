package com.microservice.dao.repository.crawler.telecom.neimenggu;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.telecom.neimenggu.TelecomNeimengguUserInfo;

@Repository
public interface TelecomNeimengguUserInfoRepository extends JpaRepository<TelecomNeimengguUserInfo, Long>{
	
	TelecomNeimengguUserInfo findByTaskid(String taskid);
	
}
 