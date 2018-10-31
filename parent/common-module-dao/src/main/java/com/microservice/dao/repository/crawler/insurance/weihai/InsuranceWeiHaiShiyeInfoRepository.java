package com.microservice.dao.repository.crawler.insurance.weihai;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.insurance.weihai.InsuranceWeiHaiShiyeInfo;
public interface InsuranceWeiHaiShiyeInfoRepository extends JpaRepository<InsuranceWeiHaiShiyeInfo, Long>{
	List<InsuranceWeiHaiShiyeInfo> findByTaskid(String taskid);
}
