package com.microservice.dao.repository.crawler.insurance.shenzhen;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.insurance.shenzhen.InsuranceShenzhenHtml;

/**
 * 深圳社保页面 Repository
 * @author rongshengxu
 *
 */
public interface InsuranceShenzhenHtmlRepository extends JpaRepository<InsuranceShenzhenHtml, Long>{

	List<InsuranceShenzhenHtml> findByTaskId(String taskId);
	
}
