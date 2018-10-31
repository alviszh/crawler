package com.microservice.dao.repository.crawler.insurance.yantai;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.insurance.yantai.InsuranceYantaiHtml;

public interface InsuranceYantaiHtmlRepository extends JpaRepository<InsuranceYantaiHtml, Long>{
	List<InsuranceYantaiHtml> findByTaskid(String taskid);
}
