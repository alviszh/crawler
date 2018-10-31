package com.microservice.dao.repository.crawler.bank.etl;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.bank.etl.BankReportOtherAttribute3;

public interface BankReportOtherAttribute3Repository extends JpaRepository<BankReportOtherAttribute3, Long>{

	List<BankReportOtherAttribute3> findByBasicUserId(String basicUserId);

}
