package com.microservice.dao.repository.crawler.telecom.fujian;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.telecom.fujian.TelecomFujianPayMsg;
import com.microservice.dao.entity.crawler.telecom.hunan.TelecomHunanPayMsg;
import com.microservice.dao.entity.crawler.telecom.neimenggu.TelecomNeimengguPayMsg;

@Repository
public interface TelecomFujianPayMsgRepository extends JpaRepository<TelecomFujianPayMsg, Long>{

}
 