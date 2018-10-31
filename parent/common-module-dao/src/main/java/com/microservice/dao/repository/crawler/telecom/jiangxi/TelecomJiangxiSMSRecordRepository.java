package com.microservice.dao.repository.crawler.telecom.jiangxi;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.telecom.jiangxi.TelecomJiangxiSMSRecord;


@Repository
public interface TelecomJiangxiSMSRecordRepository extends JpaRepository<TelecomJiangxiSMSRecord, Long> {

}

