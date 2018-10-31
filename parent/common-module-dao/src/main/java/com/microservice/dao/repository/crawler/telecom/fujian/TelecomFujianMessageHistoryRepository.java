package com.microservice.dao.repository.crawler.telecom.fujian;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.telecom.fujian.TelecomFujianMessageHistory;
import com.microservice.dao.entity.crawler.telecom.hunan.TelecomHunanMessageHistory;
import com.microservice.dao.entity.crawler.telecom.neimenggu.TelecomNeimengguMessageHistory;

@Repository
public interface TelecomFujianMessageHistoryRepository extends JpaRepository<TelecomFujianMessageHistory, Long>{

}
 