package com.microservice.dao.repository.crawler.insurance.nanjing;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.insurance.nanjing.InsuranceNanjingContractInfo;

/**
 * @description:
 * @author: sln 
 * @date: 2017年9月27日 下午4:33:19 
 */
@Repository
public interface InsuranceNanjingContractInfoRepository extends JpaRepository<InsuranceNanjingContractInfo, Long> {

}
