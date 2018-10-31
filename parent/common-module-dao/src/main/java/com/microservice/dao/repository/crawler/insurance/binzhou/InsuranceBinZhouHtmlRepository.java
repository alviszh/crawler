package com.microservice.dao.repository.crawler.insurance.binzhou;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.insurance.binzhou.InsuranceBinZhouHtml;

public interface InsuranceBinZhouHtmlRepository extends JpaRepository<InsuranceBinZhouHtml, Long>{
	List<InsuranceBinZhouHtml> findByTaskid(String taskid);
}
