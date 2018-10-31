package com.microservice.dao.repository.crawler.telecom.xinjiang;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.telecom.xinjiang.TelecomXinjiangPayMonths;

@Repository
public interface TelecomXinjiangPayMonthsRepository extends JpaRepository<TelecomXinjiangPayMonths, Long>{

}
