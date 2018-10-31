package com.microservice.dao.repository.crawler.insurance.haerbin;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.insurance.haerbin.InsuranceHaerbinShengYuInfo;
/**
 * 德州社保  Repository
 * @author qizhongbin
 *
 */
public interface InsuranceHaerbinShengYuInfoRepository extends JpaRepository<InsuranceHaerbinShengYuInfo, Long>{
	List<InsuranceHaerbinShengYuInfo> findByTaskid(String taskid);
}
