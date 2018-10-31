package com.microservice.dao.repository.crawler.telecom.fujian;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.telecom.fujian.TelecomFujianhtml;
import com.microservice.dao.entity.crawler.telecom.hunan.TelecomHunanhtml;
import com.microservice.dao.entity.crawler.telecom.neimenggu.TelecomNeimengguhtml;

@Repository
public interface TelecomFujianhtmlRepository extends JpaRepository<TelecomFujianhtml, Long>{

}
 