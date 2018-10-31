package com.microservice.dao.repository.crawler.bank.etl;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.bank.etl.BankReportCreditSalesAmount6;


public interface BankReportCreditSalesAmount6Repository extends JpaRepository<BankReportCreditSalesAmount6, Long>{

	List<BankReportCreditSalesAmount6> findByBasicUserId(String basicUserId);

}
