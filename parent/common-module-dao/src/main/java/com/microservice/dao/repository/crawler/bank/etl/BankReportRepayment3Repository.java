package com.microservice.dao.repository.crawler.bank.etl;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.bank.etl.BankReportRepayment3;

public interface BankReportRepayment3Repository extends JpaRepository<BankReportRepayment3, Long>{

	List<BankReportRepayment3> findByBasicUserId(String basicUserId);

}
