package com.microservice.dao.repository.crawler.insurance.jining;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.insurance.jining.InsuranceJiNingShengYuInfo;
public interface InsuranceJiNingShengYuInfoRepository extends JpaRepository<InsuranceJiNingShengYuInfo, Long>{
	List<InsuranceJiNingShengYuInfo> findByTaskid(String taskid);
}
