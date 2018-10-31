package com.microservice.dao.repository.crawler.insurance.shenzhen;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.insurance.shenzhen.InsuranceShenzhenPayDetail;

/**
 * 深圳社保 缴费明细 Repository
 * @author rongshengxu
 *
 */
public interface InsuranceShenzhenPayDetailRepository extends JpaRepository<InsuranceShenzhenPayDetail, Long>{

	List<InsuranceShenzhenPayDetail> findByTaskId(String taskId);
	
}
