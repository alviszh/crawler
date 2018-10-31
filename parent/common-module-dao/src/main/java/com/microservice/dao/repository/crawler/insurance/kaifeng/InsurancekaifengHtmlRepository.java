package com.microservice.dao.repository.crawler.insurance.kaifeng;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.insurance.kaifeng.InsurancekaifengHtml;

public interface InsurancekaifengHtmlRepository extends JpaRepository<InsurancekaifengHtml, Long>{
	List<InsurancekaifengHtml> findByTaskid(String taskid);
}
