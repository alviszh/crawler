package com.microservice.dao.repository.crawler.insurance.zhongshan;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.insurance.zhongshan.InsuranceZhongShanPensionAccount;

@Repository
public interface InsuranceZhongShanPensionAccountRepository extends JpaRepository<InsuranceZhongShanPensionAccount, Long> {
}
