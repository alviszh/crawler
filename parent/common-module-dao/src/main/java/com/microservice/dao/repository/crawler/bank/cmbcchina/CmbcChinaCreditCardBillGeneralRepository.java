package com.microservice.dao.repository.crawler.bank.cmbcchina;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.bank.cmbcchina.CmbcChinaCreditCardBillGeneral;

/**
 * @description:
 * @author: sln 
 * @date: 2017年11月14日 下午4:40:29 
 */
@Repository
public interface CmbcChinaCreditCardBillGeneralRepository extends JpaRepository<CmbcChinaCreditCardBillGeneral, Long> {

}
