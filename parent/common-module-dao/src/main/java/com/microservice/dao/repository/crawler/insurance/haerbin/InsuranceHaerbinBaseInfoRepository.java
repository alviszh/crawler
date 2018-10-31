package com.microservice.dao.repository.crawler.insurance.haerbin;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.insurance.haerbin.InsuranceHaerbinBaseInfo;

/**
 * 德州社保  Repository
 * @author qizhongbin
 *
 */
public interface InsuranceHaerbinBaseInfoRepository extends JpaRepository<InsuranceHaerbinBaseInfo, Long>{
	List<InsuranceHaerbinBaseInfo> findByTaskid(String taskid);
}
