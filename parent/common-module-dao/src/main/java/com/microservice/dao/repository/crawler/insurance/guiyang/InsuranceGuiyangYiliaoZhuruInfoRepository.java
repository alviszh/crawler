package com.microservice.dao.repository.crawler.insurance.guiyang;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.insurance.guiyang.InsuranceGuiyangYiliaozhuruInfo;
/**
 * 贵阳社保  Repository
 * @author qizhongbin
 *
 */
public interface InsuranceGuiyangYiliaoZhuruInfoRepository extends JpaRepository<InsuranceGuiyangYiliaozhuruInfo, Long>{
	List<InsuranceGuiyangYiliaozhuruInfo> findByTaskid(String taskid);
}
