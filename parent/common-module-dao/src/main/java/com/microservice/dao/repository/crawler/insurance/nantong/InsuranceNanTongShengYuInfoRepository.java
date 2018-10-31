package com.microservice.dao.repository.crawler.insurance.nantong;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.insurance.nantong.InsuranceNanTongShengYuInfo;
public interface InsuranceNanTongShengYuInfoRepository extends JpaRepository<InsuranceNanTongShengYuInfo, Long>{
	List<InsuranceNanTongShengYuInfo> findByTaskid(String taskid);
}
