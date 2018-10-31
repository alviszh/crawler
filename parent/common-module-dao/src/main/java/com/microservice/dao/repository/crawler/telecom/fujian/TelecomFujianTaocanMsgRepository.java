package com.microservice.dao.repository.crawler.telecom.fujian;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.telecom.fujian.TelecomFujianTaocanMsg;

@Repository
public interface TelecomFujianTaocanMsgRepository extends JpaRepository<TelecomFujianTaocanMsg, Long>{
}
 