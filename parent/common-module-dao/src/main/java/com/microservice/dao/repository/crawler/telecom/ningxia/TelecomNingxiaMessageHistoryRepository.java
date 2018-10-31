package com.microservice.dao.repository.crawler.telecom.ningxia;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.telecom.beijing.TelecomBeijingCallThremResult;
import com.microservice.dao.entity.crawler.telecom.ningxia.TelecomNingxiaMessageHistory;

@Repository
public interface TelecomNingxiaMessageHistoryRepository extends JpaRepository<TelecomNingxiaMessageHistory, Long>{

}
 