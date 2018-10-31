package com.microservice.dao.repository.crawler.insurance.weihai;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.insurance.weihai.InsuranceWeiHaiShengYuInfo;
public interface InsuranceWeiHaiShengYuInfoRepository extends JpaRepository<InsuranceWeiHaiShengYuInfo, Long>{
	List<InsuranceWeiHaiShengYuInfo> findByTaskid(String taskid);
}
