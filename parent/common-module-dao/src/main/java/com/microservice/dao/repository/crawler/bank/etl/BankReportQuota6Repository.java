package com.microservice.dao.repository.crawler.bank.etl;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.bank.etl.BankReportQuota6;

public interface BankReportQuota6Repository extends JpaRepository<BankReportQuota6, Long>{

	List<BankReportQuota6> findByBasicUserId(String basicUserId);

}
