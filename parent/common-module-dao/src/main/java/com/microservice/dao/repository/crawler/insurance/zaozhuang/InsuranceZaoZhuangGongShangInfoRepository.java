package com.microservice.dao.repository.crawler.insurance.zaozhuang;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.insurance.zaozhuang.InsuranceZaoZhuangGongShangInfo;
public interface InsuranceZaoZhuangGongShangInfoRepository extends JpaRepository<InsuranceZaoZhuangGongShangInfo, Long>{
	List<InsuranceZaoZhuangGongShangInfo> findByTaskid(String taskid);
}
