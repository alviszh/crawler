package com.microservice.dao.repository.crawler.insurance.liangshanyizu;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.insurance.liangshanyizu.InsuranceliangshanyizuHtml;

public interface InsuranceliangshanyizuHtmlRepository extends JpaRepository<InsuranceliangshanyizuHtml, Long>{
	List<InsuranceliangshanyizuHtml> findByTaskid(String taskid);
}
