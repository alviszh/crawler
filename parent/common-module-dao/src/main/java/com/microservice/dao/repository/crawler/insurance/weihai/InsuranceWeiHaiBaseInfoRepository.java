package com.microservice.dao.repository.crawler.insurance.weihai;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.insurance.weihai.InsuranceWeiHaiBaseInfo;

public interface InsuranceWeiHaiBaseInfoRepository extends JpaRepository<InsuranceWeiHaiBaseInfo, Long>{
	List<InsuranceWeiHaiBaseInfo> findByTaskid(String taskid);
}
