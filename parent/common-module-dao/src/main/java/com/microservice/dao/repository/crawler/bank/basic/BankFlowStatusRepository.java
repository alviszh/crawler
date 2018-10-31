package com.microservice.dao.repository.crawler.bank.basic;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.bank.basic.BankFlowStatus;

public interface BankFlowStatusRepository extends JpaRepository<BankFlowStatus, Long>{

}
