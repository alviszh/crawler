package com.microservice.dao.repository.crawler.insurance.weihai;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.insurance.weihai.InsuranceWeiHaiYibaoInfo;
public interface InsuranceWeiHaiYibaoInfoRepository extends JpaRepository<InsuranceWeiHaiYibaoInfo, Long>{
	List<InsuranceWeiHaiYibaoInfo> findByTaskid(String taskid);
}
