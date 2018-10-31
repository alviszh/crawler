package com.microservice.dao.repository.crawler.bank.etl;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.bank.etl.CreditCardInstallmentBill;


public interface CreditCardInstallmentBillRepository extends JpaRepository<CreditCardInstallmentBill, Long>{

	List<CreditCardInstallmentBill> findByTaskId(String taskid);
}
