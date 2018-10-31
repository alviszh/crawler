package com.microservice.dao.repository.crawler.bank.etl;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.bank.etl.BankReportDebitDetail;

public interface BankReportDebitDetailRepository extends JpaRepository<BankReportDebitDetail, Long>{

	List<BankReportDebitDetail> findByBasicUserId(String basicUserId);

}
