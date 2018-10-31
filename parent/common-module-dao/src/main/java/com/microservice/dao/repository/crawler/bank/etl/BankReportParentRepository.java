package com.microservice.dao.repository.crawler.bank.etl;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.bank.etl.BankReportParent;

public interface BankReportParentRepository extends JpaRepository<BankReportParent, Long>{

	List<BankReportParent> findByBasicUserId(String basicUserId);

}
