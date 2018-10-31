package com.microservice.dao.repository.crawler.bank.etl;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.bank.etl.BankReportRepayment12;

public interface BankReportRepayment12Repository extends JpaRepository<BankReportRepayment12, Long>{

	List<BankReportRepayment12> findByBasicUserId(String basicUserId);

}
