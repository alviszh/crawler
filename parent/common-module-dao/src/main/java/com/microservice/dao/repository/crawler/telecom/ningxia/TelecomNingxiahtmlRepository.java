package com.microservice.dao.repository.crawler.telecom.ningxia;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.telecom.beijing.TelecomBeijingCallThremResult;
import com.microservice.dao.entity.crawler.telecom.ningxia.TelecomNingxiahtml;

@Repository
public interface TelecomNingxiahtmlRepository extends JpaRepository<TelecomNingxiahtml, Long>{

}
 