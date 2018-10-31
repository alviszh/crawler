package com.microservice.dao.repository.crawler.bank.etl;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.bank.etl.BankReportInstallments;


public interface BankReportInstallmentsRepository extends JpaRepository<BankReportInstallments, Long>{

	List<BankReportInstallments> findByBasicUserId(String basicUserId);

}
