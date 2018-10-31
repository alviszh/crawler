package com.microservice.dao.repository.crawler.telecom.hunan;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.telecom.hunan.TelecomHunanMonthBillHistory;
import com.microservice.dao.entity.crawler.telecom.neimenggu.TelecomNeimengguMonthBillHistory;

@Repository
public interface TelecomHunanMonthBillHistoryRepository extends JpaRepository<TelecomHunanMonthBillHistory, Long>{

}
 