package com.microservice.dao.repository.crawler.insurance.jining;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.insurance.jining.InsuranceJiNingBaseInfo;

public interface InsuranceJiNingBaseInfoRepository extends JpaRepository<InsuranceJiNingBaseInfo, Long>{
	List<InsuranceJiNingBaseInfo> findByTaskid(String taskid);
}
