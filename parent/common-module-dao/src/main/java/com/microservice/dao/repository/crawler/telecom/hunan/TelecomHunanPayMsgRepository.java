package com.microservice.dao.repository.crawler.telecom.hunan;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.telecom.hunan.TelecomHunanPayMsg;
import com.microservice.dao.entity.crawler.telecom.neimenggu.TelecomNeimengguPayMsg;

@Repository
public interface TelecomHunanPayMsgRepository extends JpaRepository<TelecomHunanPayMsg, Long>{

}
 