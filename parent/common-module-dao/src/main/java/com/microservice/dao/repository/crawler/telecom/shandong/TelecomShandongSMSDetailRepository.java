package com.microservice.dao.repository.crawler.telecom.shandong;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.telecom.shandong.TelecomShandongSMSDetail;

@Repository
public interface TelecomShandongSMSDetailRepository extends JpaRepository<TelecomShandongSMSDetail, Long>{

}
 