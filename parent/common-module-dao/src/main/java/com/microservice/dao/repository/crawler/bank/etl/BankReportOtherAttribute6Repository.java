package com.microservice.dao.repository.crawler.bank.etl;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.bank.etl.BankReportOtherAttribute6;

public interface BankReportOtherAttribute6Repository extends JpaRepository<BankReportOtherAttribute6, Long>{

	List<BankReportOtherAttribute6> findByBasicUserId(String basicUserId);

}
