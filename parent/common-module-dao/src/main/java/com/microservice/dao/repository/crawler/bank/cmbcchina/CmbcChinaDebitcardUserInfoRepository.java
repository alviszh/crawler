package com.microservice.dao.repository.crawler.bank.cmbcchina;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.bank.cmbcchina.CmbcChinaDebitcardUserInfo;


/**
 * @description:
 * @author: sln 
 * @date: 2017年11月2日 上午10:18:38 
 */
@Repository
public interface CmbcChinaDebitcardUserInfoRepository extends JpaRepository<CmbcChinaDebitcardUserInfo, Long> {

}
