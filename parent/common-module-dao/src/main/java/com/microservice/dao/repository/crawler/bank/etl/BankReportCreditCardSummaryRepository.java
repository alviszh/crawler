package com.microservice.dao.repository.crawler.bank.etl;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.bank.etl.BankReportCreditCardSummary;

public interface BankReportCreditCardSummaryRepository extends JpaRepository<BankReportCreditCardSummary, Long>{

	List<BankReportCreditCardSummary> findByBasicUserId(String basicUserId);

}
