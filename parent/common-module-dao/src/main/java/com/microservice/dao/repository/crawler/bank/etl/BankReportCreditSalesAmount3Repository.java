package com.microservice.dao.repository.crawler.bank.etl;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.bank.etl.BankReportCreditSalesAmount3;


public interface BankReportCreditSalesAmount3Repository extends JpaRepository<BankReportCreditSalesAmount3, Long>{

	List<BankReportCreditSalesAmount3> findByBasicUserId(String basicUserId);

}
