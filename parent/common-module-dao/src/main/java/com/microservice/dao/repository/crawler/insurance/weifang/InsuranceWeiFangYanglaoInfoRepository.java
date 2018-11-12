package com.microservice.dao.repository.crawler.insurance.weifang;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.insurance.weifang.InsuranceWeiFangYanglaoInfo;
/**
 * 济南社保  Repository
 * @author qizhongbin
 *
 */
public interface InsuranceWeiFangYanglaoInfoRepository extends JpaRepository<InsuranceWeiFangYanglaoInfo, Long>{
	List<InsuranceWeiFangYanglaoInfo> findByTaskid(String taskid);
}
