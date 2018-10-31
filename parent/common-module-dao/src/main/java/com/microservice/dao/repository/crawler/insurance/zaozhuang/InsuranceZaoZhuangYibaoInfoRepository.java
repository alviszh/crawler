package com.microservice.dao.repository.crawler.insurance.zaozhuang;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.insurance.zaozhuang.InsuranceZaoZhuangYibaoInfo;
public interface InsuranceZaoZhuangYibaoInfoRepository extends JpaRepository<InsuranceZaoZhuangYibaoInfo, Long>{
	List<InsuranceZaoZhuangYibaoInfo> findByTaskid(String taskid);
}
