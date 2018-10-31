package com.microservice.dao.repository.crawler.bank.etl;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.bank.etl.BankReportCreditBills;


public interface BankReportCreditBillsRepository extends JpaRepository<BankReportCreditBills, Long>{

	List<BankReportCreditBills> findByBasicUserId(String basicUserId);

}
