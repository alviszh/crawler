package com.microservice.dao.repository.crawler.bank.spdb;

import com.microservice.dao.entity.crawler.bank.spdb.SpdbCreditCardGeneralInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpdbCreditCardGeneralInfoRepository extends JpaRepository<SpdbCreditCardGeneralInfo, Long>{
}
