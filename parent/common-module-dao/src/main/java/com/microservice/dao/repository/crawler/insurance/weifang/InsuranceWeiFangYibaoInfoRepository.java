package com.microservice.dao.repository.crawler.insurance.weifang;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.insurance.weifang.InsuranceWeiFangYibaoInfo;
/**
 * 济南社保  Repository
 * @author qizhongbin
 *
 */
public interface InsuranceWeiFangYibaoInfoRepository extends JpaRepository<InsuranceWeiFangYibaoInfo, Long>{
	List<InsuranceWeiFangYibaoInfo> findByTaskid(String taskid);
}
