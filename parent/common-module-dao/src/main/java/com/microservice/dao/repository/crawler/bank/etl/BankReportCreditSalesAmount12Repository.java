package com.microservice.dao.repository.crawler.bank.etl;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.bank.etl.BankReportCreditSalesAmount12;


public interface BankReportCreditSalesAmount12Repository extends JpaRepository<BankReportCreditSalesAmount12, Long>{

	List<BankReportCreditSalesAmount12> findByBasicUserId(String basicUserId);

}
