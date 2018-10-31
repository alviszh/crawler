package com.microservice.dao.repository.crawler.bank.etl;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.bank.etl.BankReportQuota3;

public interface BankReportQuota3Repository extends JpaRepository<BankReportQuota3, Long>{

	List<BankReportQuota3> findByBasicUserId(String basicUserId);

}
