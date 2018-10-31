package com.microservice.dao.repository.crawler.telecom.jilin;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.telecom.jilin.TelecomJilinSMSDetails;

@Repository
public interface TelecomJilinSMSDetailsRepository extends JpaRepository<TelecomJilinSMSDetails, Long>{

}
 