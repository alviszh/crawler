package com.microservice.dao.repository.crawler.telecom.hubei.wap;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.telecom.hubei.wap.TelecomHubeiWapPointrecord;

@Repository
public interface TelecomHubeiWapPointrecordRepository extends JpaRepository<TelecomHubeiWapPointrecord, Long>{

}
