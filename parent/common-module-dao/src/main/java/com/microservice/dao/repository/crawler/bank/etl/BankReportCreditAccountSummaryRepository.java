package com.microservice.dao.repository.crawler.bank.etl;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.bank.etl.BankReportCreditAccountSummary;

public interface BankReportCreditAccountSummaryRepository extends JpaRepository<BankReportCreditAccountSummary, Long>{

	List<BankReportCreditAccountSummary> findByBasicUserId(String basicUserId);

}
