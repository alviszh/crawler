package com.microservice.dao.repository.crawler.bank.psbcchina;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.bank.psbcchina.PsbcChinaDebitCardUserInfo;

public interface PsbcChinaDebitCardUserInfoRepository  extends JpaRepository<PsbcChinaDebitCardUserInfo, Long>{
	

}
