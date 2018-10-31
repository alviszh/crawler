package com.microservice.dao.repository.crawler.insurance.jining;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.insurance.jining.InsuranceJiNingGongShangInfo;
public interface InsuranceJiNingGongShangInfoRepository extends JpaRepository<InsuranceJiNingGongShangInfo, Long>{
	List<InsuranceJiNingGongShangInfo> findByTaskid(String taskid);
}
