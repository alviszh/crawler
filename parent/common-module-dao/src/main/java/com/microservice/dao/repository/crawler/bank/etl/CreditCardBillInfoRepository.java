package com.microservice.dao.repository.crawler.bank.etl;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.bank.etl.CreditCardBillInfo;


public interface CreditCardBillInfoRepository extends JpaRepository<CreditCardBillInfo, Long>{

	List<CreditCardBillInfo> findByTaskId(String taskid);
}
