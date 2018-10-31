package com.microservice.dao.repository.crawler.insurance.fuyang;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.insurance.fuyang.InsurancefuyangHtml;

public interface InsurancefuyangHtmlRepository extends JpaRepository<InsurancefuyangHtml, Long>{
	List<InsurancefuyangHtml> findByTaskid(String taskid);
}
