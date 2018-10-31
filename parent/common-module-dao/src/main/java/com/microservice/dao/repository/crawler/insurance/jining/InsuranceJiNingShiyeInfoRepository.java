package com.microservice.dao.repository.crawler.insurance.jining;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.insurance.jining.InsuranceJiNingShiyeInfo;
public interface InsuranceJiNingShiyeInfoRepository extends JpaRepository<InsuranceJiNingShiyeInfo, Long>{
	List<InsuranceJiNingShiyeInfo> findByTaskid(String taskid);
}
