package com.microservice.dao.repository.crawler.bank.etl;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.bank.etl.BankReportOverdueCreditcard;

public interface BankReportOverdueCreditcardRepository extends JpaRepository<BankReportOverdueCreditcard, Long>{

	List<BankReportOverdueCreditcard> findByBasicUserId(String basicUserId);

}
