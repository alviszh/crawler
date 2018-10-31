package com.microservice.dao.repository.crawler.bank.etl;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.bank.etl.BankReportQuota12;

public interface BankReportQuota12Repository extends JpaRepository<BankReportQuota12, Long>{

	List<BankReportQuota12> findByBasicUserId(String basicUserId);

}
