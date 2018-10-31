package com.microservice.dao.repository.crawler.bank.cmbcchina;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.bank.cmbcchina.CmbcChinaDebitCardHtml;

/**
 * @description:
 * @author: sln 
 * @date: 2017年11月2日 上午10:16:49 
 */
@Repository
public interface CmbcChinaDebitCardHtmlRepository extends JpaRepository<CmbcChinaDebitCardHtml, Long> {

}
