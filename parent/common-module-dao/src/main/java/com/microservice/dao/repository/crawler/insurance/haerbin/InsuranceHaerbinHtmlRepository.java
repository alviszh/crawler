package com.microservice.dao.repository.crawler.insurance.haerbin;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.insurance.haerbin.InsuranceHaerbinHtml;

public interface InsuranceHaerbinHtmlRepository extends JpaRepository<InsuranceHaerbinHtml, Long>{
	List<InsuranceHaerbinHtml> findByTaskid(String taskid);
}
