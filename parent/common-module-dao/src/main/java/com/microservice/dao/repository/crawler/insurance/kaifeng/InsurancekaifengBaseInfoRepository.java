package com.microservice.dao.repository.crawler.insurance.kaifeng;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.insurance.kaifeng.InsurancekaifengBaseInfo;

public interface InsurancekaifengBaseInfoRepository extends JpaRepository<InsurancekaifengBaseInfo, Long>{
	List<InsurancekaifengBaseInfo> findByTaskid(String taskid);
}
