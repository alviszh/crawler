package com.microservice.dao.repository.crawler.bank.cmbcchina;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.bank.cmbcchina.CmbcChinaCreditCardBillDetail;

/**
 * @description:
 * @author: sln 
 * @date: 2017年11月14日 下午4:39:38 
 */
@Repository
public interface CmbcChinaCreditCardBillDetailRepository extends JpaRepository<CmbcChinaCreditCardBillDetail, Long> {

}
