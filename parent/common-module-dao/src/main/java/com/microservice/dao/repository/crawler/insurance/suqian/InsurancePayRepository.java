package com.microservice.dao.repository.crawler.insurance.suqian;

import com.microservice.dao.entity.crawler.insurance.suqian.InsurancePay;

import org.springframework.data.jpa.repository.JpaRepository;


/**
 * Created by root on 2017/9/18.
 */
public interface InsurancePayRepository extends JpaRepository<InsurancePay, Long> {
}
