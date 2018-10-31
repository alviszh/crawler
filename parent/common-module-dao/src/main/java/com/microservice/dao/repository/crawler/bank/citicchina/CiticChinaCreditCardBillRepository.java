package com.microservice.dao.repository.crawler.bank.citicchina;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.bank.citicchina.CiticChinaCreditCardBill;

@Repository
public interface CiticChinaCreditCardBillRepository extends JpaRepository<CiticChinaCreditCardBill,Long>{

}
