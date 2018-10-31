package com.microservice.dao.repository.crawler.bank.spdb;


import com.microservice.dao.entity.crawler.bank.spdb.SpdbDebitCardUserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpdbDebitCardUserInfoRepository extends JpaRepository<SpdbDebitCardUserInfo, Long>{
}
