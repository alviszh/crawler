package com.microservice.dao.repository.crawler.insurance.nantong;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.insurance.nantong.InsuranceNanTongGongShangInfo;
public interface InsuranceNanTongGongShangInfoRepository extends JpaRepository<InsuranceNanTongGongShangInfo, Long>{
	List<InsuranceNanTongGongShangInfo> findByTaskid(String taskid);
}
