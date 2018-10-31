package com.microservice.dao.repository.crawler.telecom.beijing;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.telecom.beijing.TelecomBeijingSMSThremResult;

@Repository
public interface TelecomBeijingSMSThremResultRepository extends JpaRepository<TelecomBeijingSMSThremResult, Long>{

}
 