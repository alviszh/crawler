package com.microservice.dao.repository.crawler.telecom.hunan;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.telecom.hunan.TelecomHunanZengzhiMsg;

@Repository
public interface TelecomHunanZengzhiMsgRepository extends JpaRepository<TelecomHunanZengzhiMsg, Long>{

}
 