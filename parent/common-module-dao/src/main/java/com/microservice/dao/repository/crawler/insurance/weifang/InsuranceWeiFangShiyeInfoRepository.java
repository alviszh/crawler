package com.microservice.dao.repository.crawler.insurance.weifang;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.insurance.weifang.InsuranceWeiFangShiyeInfo;
/**
 * 济南社保  Repository
 * @author qizhongbin
 *
 */
public interface InsuranceWeiFangShiyeInfoRepository extends JpaRepository<InsuranceWeiFangShiyeInfo, Long>{
	List<InsuranceWeiFangShiyeInfo> findByTaskid(String taskid);
}
