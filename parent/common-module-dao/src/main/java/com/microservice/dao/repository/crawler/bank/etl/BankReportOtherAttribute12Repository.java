package com.microservice.dao.repository.crawler.bank.etl;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.bank.etl.BankReportOtherAttribute12;

public interface BankReportOtherAttribute12Repository extends JpaRepository<BankReportOtherAttribute12, Long>{

	List<BankReportOtherAttribute12> findByBasicUserId(String basicUserId);

}
