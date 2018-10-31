package com.microservice.dao.repository.crawler.insurance.liangshanyizu;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.insurance.liangshanyizu.InsuranceliangshanyizuBaseInfo;

public interface InsuranceliangshanyizuBaseInfoRepository extends JpaRepository<InsuranceliangshanyizuBaseInfo, Long>{
	List<InsuranceliangshanyizuBaseInfo> findByTaskid(String taskid);
}
