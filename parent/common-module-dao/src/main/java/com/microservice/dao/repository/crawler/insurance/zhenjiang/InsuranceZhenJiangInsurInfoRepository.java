package com.microservice.dao.repository.crawler.insurance.zhenjiang;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.insurance.zhenjiang.InsuranceZhenJiangInsurInfo;

@Repository
public interface InsuranceZhenJiangInsurInfoRepository extends JpaRepository<InsuranceZhenJiangInsurInfo, Long> {

}
