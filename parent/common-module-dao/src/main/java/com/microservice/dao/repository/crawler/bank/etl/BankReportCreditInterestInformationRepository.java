package com.microservice.dao.repository.crawler.bank.etl;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.bank.etl.BankReportCreditInterestInformation;

public interface BankReportCreditInterestInformationRepository extends JpaRepository<BankReportCreditInterestInformation, Long>{

	List<BankReportCreditInterestInformation> findByBasicUserId(String basicUserId);

}
