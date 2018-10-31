package com.microservice.dao.repository.crawler.telecom.heilongjiang;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.telecom.heilongjiang.TelecomPayMsgThemResult;
@Repository
public interface TelecomPayMsgThemResultRepository extends JpaRepository<TelecomPayMsgThemResult, Long>{
	List<TelecomPayMsgThemResult> findByTaskid(String taskId);
}
 