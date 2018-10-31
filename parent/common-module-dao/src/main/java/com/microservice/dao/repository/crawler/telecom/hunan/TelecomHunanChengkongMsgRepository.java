package com.microservice.dao.repository.crawler.telecom.hunan;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.telecom.hunan.TelecomHunanChengkongMsg;

@Repository
public interface TelecomHunanChengkongMsgRepository extends JpaRepository<TelecomHunanChengkongMsg, Long>{
	
}
 