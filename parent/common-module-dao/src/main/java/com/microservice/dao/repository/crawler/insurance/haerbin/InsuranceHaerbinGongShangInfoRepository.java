package com.microservice.dao.repository.crawler.insurance.haerbin;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.insurance.haerbin.InsuranceHaerbinGongShangInfo;
/**
 * 德州社保  Repository
 * @author qizhongbin
 *
 */
public interface InsuranceHaerbinGongShangInfoRepository extends JpaRepository<InsuranceHaerbinGongShangInfo, Long>{
	List<InsuranceHaerbinGongShangInfo> findByTaskid(String taskid);
}
