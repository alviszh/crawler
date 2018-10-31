package com.microservice.dao.repository.crawler.bank.spdb;

import com.microservice.dao.entity.crawler.bank.spdb.SpdbCreditCardInstallment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpdbCreditCardInstallmentRepository extends JpaRepository<SpdbCreditCardInstallment, Long>{
}
