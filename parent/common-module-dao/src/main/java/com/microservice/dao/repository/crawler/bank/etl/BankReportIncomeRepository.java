package com.microservice.dao.repository.crawler.bank.etl;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.bank.etl.BankReportIncome;

public interface BankReportIncomeRepository extends JpaRepository<BankReportIncome, Long>{

	List<BankReportIncome> findByBasicUserId(String basicUserId);

}
