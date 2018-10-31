package com.microservice.dao.repository.crawler.insurance.mianyang;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.insurance.mianyang.InsurancemianyangHtml;

public interface InsurancemianyangHtmlRepository extends JpaRepository<InsurancemianyangHtml, Long>{
	List<InsurancemianyangHtml> findByTaskid(String taskid);
}
