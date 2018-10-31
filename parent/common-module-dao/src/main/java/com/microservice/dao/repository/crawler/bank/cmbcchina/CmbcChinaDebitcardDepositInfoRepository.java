package com.microservice.dao.repository.crawler.bank.cmbcchina;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.bank.cmbcchina.CmbcChinaDebitcardDepositInfo;

/**
 * @description:
 * @author: sln 
 * @date: 2017年12月1日 上午11:24:30 
 */
@Repository
public interface CmbcChinaDebitcardDepositInfoRepository extends JpaRepository<CmbcChinaDebitcardDepositInfo, Long> {

}
