package com.microservice.dao.repository.crawler.insurance.nantong;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.insurance.nantong.InsuranceNanTongYibaoInfo;
public interface InsuranceNanTongYibaoInfoRepository extends JpaRepository<InsuranceNanTongYibaoInfo, Long>{
	List<InsuranceNanTongYibaoInfo> findByTaskid(String taskid);
}
