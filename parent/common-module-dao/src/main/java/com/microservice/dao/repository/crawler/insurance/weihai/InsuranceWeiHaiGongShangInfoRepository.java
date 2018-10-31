package com.microservice.dao.repository.crawler.insurance.weihai;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.insurance.weihai.InsuranceWeiHaiGongShangInfo;
public interface InsuranceWeiHaiGongShangInfoRepository extends JpaRepository<InsuranceWeiHaiGongShangInfo, Long>{
	List<InsuranceWeiHaiGongShangInfo> findByTaskid(String taskid);
}
