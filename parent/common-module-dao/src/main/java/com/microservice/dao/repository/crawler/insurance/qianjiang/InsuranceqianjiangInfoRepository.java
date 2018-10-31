package com.microservice.dao.repository.crawler.insurance.qianjiang;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.insurance.qianjiang.InsuranceqianjiangInfo;
public interface InsuranceqianjiangInfoRepository extends JpaRepository<InsuranceqianjiangInfo, Long>{
	List<InsuranceqianjiangInfo> findByTaskid(String taskid);
}
