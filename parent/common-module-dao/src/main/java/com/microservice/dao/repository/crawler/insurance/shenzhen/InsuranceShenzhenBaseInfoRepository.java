package com.microservice.dao.repository.crawler.insurance.shenzhen;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.insurance.shenzhen.InsuranceShenzhenBaseInfo;

/**
 * 深圳社保 参保基本信息 Repository
 * @author rongshengxu
 *
 */
public interface InsuranceShenzhenBaseInfoRepository extends JpaRepository<InsuranceShenzhenBaseInfo, Long>{

	List<InsuranceShenzhenBaseInfo> findByTaskId(String taskId);
	
}
