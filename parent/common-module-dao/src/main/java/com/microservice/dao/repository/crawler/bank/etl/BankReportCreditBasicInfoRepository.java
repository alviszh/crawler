package com.microservice.dao.repository.crawler.bank.etl;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.bank.etl.BankReportCreditBasicInfo;

public interface BankReportCreditBasicInfoRepository extends JpaRepository<BankReportCreditBasicInfo, Long>{

	List<BankReportCreditBasicInfo> findByBasicUserId(String basicUserId);

}
