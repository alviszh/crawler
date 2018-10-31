package com.microservice.dao.repository.crawler.insurance.nantong;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.insurance.nantong.InsuranceNanTongBaseInfo;

public interface InsuranceNanTongBaseInfoRepository extends JpaRepository<InsuranceNanTongBaseInfo, Long>{
	List<InsuranceNanTongBaseInfo> findByTaskid(String taskid);
}
