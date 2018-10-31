package com.microservice.dao.repository.crawler.insurance.jinan;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.insurance.jinan.InsuranceJiNanHtml;

public interface InsuranceJiNanHtmlRepository extends JpaRepository<InsuranceJiNanHtml, Long>{
	List<InsuranceJiNanHtml> findByTaskid(String taskid);
}
