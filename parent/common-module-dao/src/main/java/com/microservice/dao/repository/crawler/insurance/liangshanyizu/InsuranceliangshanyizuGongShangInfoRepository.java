package com.microservice.dao.repository.crawler.insurance.liangshanyizu;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.insurance.liangshanyizu.InsuranceliangshanyizuGongShangInfo;
public interface InsuranceliangshanyizuGongShangInfoRepository extends JpaRepository<InsuranceliangshanyizuGongShangInfo, Long>{
	List<InsuranceliangshanyizuGongShangInfo> findByTaskid(String taskid);
}
