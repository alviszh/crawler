package com.microservice.dao.repository.crawler.insurance.kaifeng;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.insurance.kaifeng.InsurancekaifengGongShangInfo;
public interface InsurancekaifengGongShangInfoRepository extends JpaRepository<InsurancekaifengGongShangInfo, Long>{
	List<InsurancekaifengGongShangInfo> findByTaskid(String taskid);
}
