package com.microservice.dao.repository.crawler.insurance.guangzhou;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.insurance.guangzhou.InsuranceGuangzhouHtml;

public interface InsuranceGuangzhouHtmlRepository extends JpaRepository<InsuranceGuangzhouHtml, Long>{

	List<InsuranceGuangzhouHtml> findByTaskid(String taskid);
	
}
