package com.microservice.dao.repository.crawler.insurance.weifang;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.insurance.weifang.InsuranceWeiFangBaseInfo;

/**
 * 潍坊社保  Repository
 *
 */
public interface InsuranceWeiFangBaseInfoRepository extends JpaRepository<InsuranceWeiFangBaseInfo, Long>{
	List<InsuranceWeiFangBaseInfo> findByTaskid(String taskid);
}
