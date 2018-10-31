package com.microservice.dao.repository.crawler.bank.etl;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.bank.etl.BankReportDebitDeposit;

public interface BankReportDebitDepositRepository extends JpaRepository<BankReportDebitDeposit, Long>{

	List<BankReportDebitDeposit> findByBasicUserId(String basicUserId);

}
