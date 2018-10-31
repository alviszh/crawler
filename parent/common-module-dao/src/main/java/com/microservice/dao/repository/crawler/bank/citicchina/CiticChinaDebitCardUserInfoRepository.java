package com.microservice.dao.repository.crawler.bank.citicchina;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.bank.citicchina.CiticChinaDebitCardUserInfo;

@Repository
public interface CiticChinaDebitCardUserInfoRepository extends JpaRepository<CiticChinaDebitCardUserInfo,Long>{

}
