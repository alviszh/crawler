package com.microservice.dao.repository.crawler.telecom.shanxi3;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.telecom.shanxi3.TelecomShanxi3MonthBill;

/**
 * @Description
 * @author sln
 * @date 2017年9月7日 上午11:37:20
 */
@Repository
public interface TelecomShanxi3MonthBillRepository extends JpaRepository<TelecomShanxi3MonthBill, Long> {

}

