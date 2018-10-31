package com.microservice.dao.repository.crawler.insurance.qianjiang;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.insurance.qianjiang.InsuranceqianjiangBaseInfo;

public interface InsuranceqianjiangBaseInfoRepository extends JpaRepository<InsuranceqianjiangBaseInfo, Long>{
	List<InsuranceqianjiangBaseInfo> findByTaskid(String taskid);
}
