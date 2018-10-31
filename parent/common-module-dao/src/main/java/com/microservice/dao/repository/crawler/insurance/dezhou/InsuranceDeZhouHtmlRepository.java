package com.microservice.dao.repository.crawler.insurance.dezhou;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.insurance.dezhou.InsuranceDeZhouHtml;

public interface InsuranceDeZhouHtmlRepository extends JpaRepository<InsuranceDeZhouHtml, Long>{
	List<InsuranceDeZhouHtml> findByTaskid(String taskid);
}
