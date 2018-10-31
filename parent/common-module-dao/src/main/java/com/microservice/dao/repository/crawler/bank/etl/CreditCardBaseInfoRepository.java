package com.microservice.dao.repository.crawler.bank.etl;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.bank.etl.CreditCardBaseInfo;

public interface CreditCardBaseInfoRepository extends JpaRepository<CreditCardBaseInfo, Long>{

	List<CreditCardBaseInfo> findByTaskId(String taskid);
}
