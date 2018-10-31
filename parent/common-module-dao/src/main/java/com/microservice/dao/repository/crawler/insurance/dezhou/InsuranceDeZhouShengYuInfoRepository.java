package com.microservice.dao.repository.crawler.insurance.dezhou;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.insurance.dezhou.InsuranceDeZhouShengYuInfo;
/**
 * 德州社保  Repository
 * @author qizhongbin
 *
 */
public interface InsuranceDeZhouShengYuInfoRepository extends JpaRepository<InsuranceDeZhouShengYuInfo, Long>{
	List<InsuranceDeZhouShengYuInfo> findByTaskid(String taskid);
}
