package com.microservice.dao.repository.crawler.insurance.nanjing;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.insurance.nanjing.InsuranceNanjingAllChargeInfo;

/**
 * @description:
 * @author: sln 
 * @date: 2017年9月27日 上午9:54:14 
 */
@Repository
public interface InsuranceNanjingAllChargeInfoRepository extends JpaRepository<InsuranceNanjingAllChargeInfo, Long> {

}
