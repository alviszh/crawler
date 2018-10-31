package com.microservice.dao.repository.crawler.insurance.liangshanyizu;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.insurance.liangshanyizu.InsuranceliangshanyizuShengYuInfo;
public interface InsuranceliangshanyizuShengYuInfoRepository extends JpaRepository<InsuranceliangshanyizuShengYuInfo, Long>{
	List<InsuranceliangshanyizuShengYuInfo> findByTaskid(String taskid);
}
