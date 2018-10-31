package com.microservice.dao.repository.crawler.telecom.qinghai;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.telecom.beijing.TelecomBeijingPayResult;
import com.microservice.dao.entity.crawler.telecom.qinghai.TelecomQingHaiPayResult;

@Repository
public interface TelecomQingHaiPayResultRepository extends JpaRepository<TelecomQingHaiPayResult, Long>{

}
 