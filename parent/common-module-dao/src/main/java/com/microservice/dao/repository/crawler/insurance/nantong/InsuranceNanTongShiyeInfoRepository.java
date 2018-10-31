package com.microservice.dao.repository.crawler.insurance.nantong;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.insurance.nantong.InsuranceNanTongShiyeInfo;
public interface InsuranceNanTongShiyeInfoRepository extends JpaRepository<InsuranceNanTongShiyeInfo, Long>{
	List<InsuranceNanTongShiyeInfo> findByTaskid(String taskid);
}
