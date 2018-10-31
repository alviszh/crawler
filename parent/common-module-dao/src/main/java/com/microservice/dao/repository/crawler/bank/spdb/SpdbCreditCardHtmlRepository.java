package com.microservice.dao.repository.crawler.bank.spdb;

import com.microservice.dao.entity.crawler.bank.spdb.SpdbCreditCardHtml;
import com.microservice.dao.entity.crawler.bank.spdb.SpdbDebitCardHtml;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpdbCreditCardHtmlRepository extends JpaRepository<SpdbCreditCardHtml, Long>{
}
