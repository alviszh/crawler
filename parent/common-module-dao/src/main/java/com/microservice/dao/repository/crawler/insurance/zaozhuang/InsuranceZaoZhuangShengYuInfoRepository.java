package com.microservice.dao.repository.crawler.insurance.zaozhuang;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.insurance.zaozhuang.InsuranceZaoZhuangShengYuInfo;
public interface InsuranceZaoZhuangShengYuInfoRepository extends JpaRepository<InsuranceZaoZhuangShengYuInfo, Long>{
	List<InsuranceZaoZhuangShengYuInfo> findByTaskid(String taskid);
}
