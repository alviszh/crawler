package com.microservice.dao.repository.crawler.insurance.jining;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.insurance.jining.InsuranceJiNingHtml;

public interface InsuranceJiNingHtmlRepository extends JpaRepository<InsuranceJiNingHtml, Long>{
	List<InsuranceJiNingHtml> findByTaskid(String taskid);
}
