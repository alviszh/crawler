package com.microservice.dao.repository.crawler.bank.etl;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.bank.etl.BankReportCreditRepaymentSummary;

public interface BankReportCreditRepaymentSummaryRepository extends JpaRepository<BankReportCreditRepaymentSummary, Long>{

	List<BankReportCreditRepaymentSummary> findByBasicUserId(String basicUserId);

}
