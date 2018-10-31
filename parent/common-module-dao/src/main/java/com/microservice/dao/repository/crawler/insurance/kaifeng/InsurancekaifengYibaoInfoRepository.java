package com.microservice.dao.repository.crawler.insurance.kaifeng;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.insurance.kaifeng.InsurancekaifengYibaoInfo;
public interface InsurancekaifengYibaoInfoRepository extends JpaRepository<InsurancekaifengYibaoInfo, Long>{
	List<InsurancekaifengYibaoInfo> findByTaskid(String taskid);
}
