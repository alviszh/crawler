package com.microservice.dao.repository.crawler.insurance.weifang;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.insurance.weifang.InsuranceWeiFangShengYuInfo;
/**
 * 济南社保  Repository
 * @author qizhongbin
 *
 */
public interface InsuranceWeiFangShengYuInfoRepository extends JpaRepository<InsuranceWeiFangShengYuInfo, Long>{
	List<InsuranceWeiFangShengYuInfo> findByTaskid(String taskid);
}
