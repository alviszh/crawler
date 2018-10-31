package com.microservice.dao.repository.crawler.bank.spdb;

import com.microservice.dao.entity.crawler.bank.spdb.SpdbCreditCardBillDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpdbCreditCardBillDetailRepository extends JpaRepository<SpdbCreditCardBillDetail, Long>{
}
