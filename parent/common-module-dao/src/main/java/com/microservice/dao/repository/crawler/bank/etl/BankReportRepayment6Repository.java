package com.microservice.dao.repository.crawler.bank.etl;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.bank.etl.BankReportRepayment6;

public interface BankReportRepayment6Repository extends JpaRepository<BankReportRepayment6, Long>{

	List<BankReportRepayment6> findByBasicUserId(String basicUserId);

}
