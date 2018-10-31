package com.microservice.dao.repository.crawler.insurance.guiyang;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.insurance.guiyang.InsuranceGuiyangHtml;

public interface InsuranceGuiyangHtmlRepository extends JpaRepository<InsuranceGuiyangHtml, Long>{
	List<InsuranceGuiyangHtml> findByTaskid(String taskid);
}
