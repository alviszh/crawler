package com.microservice.dao.repository.crawler.telecom.henan;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.telecom.henan.TelecomHenanSMSDetail;

@Repository
public interface TelecomHenanSMSDetailRepository extends JpaRepository<TelecomHenanSMSDetail, Long>{

}
 