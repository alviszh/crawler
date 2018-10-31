package com.microservice.dao.repository.crawler.insurance.jining;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.insurance.jining.InsuranceJiNingYibaoInfo;
public interface InsuranceJiNingYibaoInfoRepository extends JpaRepository<InsuranceJiNingYibaoInfo, Long>{
	List<InsuranceJiNingYibaoInfo> findByTaskid(String taskid);
}
