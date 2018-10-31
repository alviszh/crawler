package com.microservice.dao.repository.crawler.bank.citicchina;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.bank.citicchina.CiticChinaCreditCardUserInfo;

@Repository
public interface CiticChinaCreditCardUserInfoRepository extends JpaRepository<CiticChinaCreditCardUserInfo,Long>{

}
