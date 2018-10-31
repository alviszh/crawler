package com.microservice.dao.repository.crawler.insurance.liangshanyizu;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.insurance.liangshanyizu.InsuranceliangshanyizuShiyeInfo;
public interface InsuranceliangshanyizuShiyeInfoRepository extends JpaRepository<InsuranceliangshanyizuShiyeInfo, Long>{
	List<InsuranceliangshanyizuShiyeInfo> findByTaskid(String taskid);
}
