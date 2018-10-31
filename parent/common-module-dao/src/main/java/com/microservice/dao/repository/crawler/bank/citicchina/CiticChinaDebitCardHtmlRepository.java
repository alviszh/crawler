package com.microservice.dao.repository.crawler.bank.citicchina;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.bank.citicchina.CiticChinaDebitCardHtml;

@Repository
public interface CiticChinaDebitCardHtmlRepository extends JpaRepository<CiticChinaDebitCardHtml, Long>{

}
