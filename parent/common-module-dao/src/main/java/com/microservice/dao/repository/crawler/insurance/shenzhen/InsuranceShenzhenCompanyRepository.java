package com.microservice.dao.repository.crawler.insurance.shenzhen;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.insurance.shenzhen.InsuranceShenzhenCompany;

/**
 * 深圳社保 单位信息 Repository
 * @author rongshengxu
 *
 */
public interface InsuranceShenzhenCompanyRepository extends JpaRepository<InsuranceShenzhenCompany, Long>{

	List<InsuranceShenzhenCompany> findByTaskId(String taskId);
	
}
