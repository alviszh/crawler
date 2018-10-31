package com.microservice.dao.repository.crawler.telecom.shanxi3;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.telecom.shanxi3.TelecomShanxi3SMSRecord;

/**
 * @Description
 * @author sln
 * @date 2017年8月23日 下午3:21:20
 */
@Repository
public interface TelecomShanxi3SMSRecordRepository extends JpaRepository<TelecomShanxi3SMSRecord, Long> {

}

