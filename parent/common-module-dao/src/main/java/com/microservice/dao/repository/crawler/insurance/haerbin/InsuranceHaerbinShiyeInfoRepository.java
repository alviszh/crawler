package com.microservice.dao.repository.crawler.insurance.haerbin;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.insurance.haerbin.InsuranceHaerbinShiyeInfo;
/**
 * 德州社保  Repository
 * @author qizhongbin
 *
 */
public interface InsuranceHaerbinShiyeInfoRepository extends JpaRepository<InsuranceHaerbinShiyeInfo, Long>{
	List<InsuranceHaerbinShiyeInfo> findByTaskid(String taskid);
}
