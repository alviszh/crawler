package com.microservice.dao.repository.crawler.bank.etl;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.bank.etl.BankReportCreditOverdueInformation;

public interface BankReportCreditOverdueInformationRepository extends JpaRepository<BankReportCreditOverdueInformation, Long>{

	List<BankReportCreditOverdueInformation> findByBasicUserId(String basicUserId);

}
