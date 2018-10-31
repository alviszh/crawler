package com.microservice.dao.repository.crawler.insurance.zaozhuang;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.insurance.zaozhuang.InsuranceZaoZhuangBaseInfo;

public interface InsuranceZaoZhuangBaseInfoRepository extends JpaRepository<InsuranceZaoZhuangBaseInfo, Long>{
	List<InsuranceZaoZhuangBaseInfo> findByTaskid(String taskid);
}
