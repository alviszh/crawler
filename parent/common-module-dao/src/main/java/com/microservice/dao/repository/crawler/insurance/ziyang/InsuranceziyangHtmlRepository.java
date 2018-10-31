package com.microservice.dao.repository.crawler.insurance.ziyang;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.insurance.ziyang.InsuranceziyangHtml;

public interface InsuranceziyangHtmlRepository extends JpaRepository<InsuranceziyangHtml, Long>{
	List<InsuranceziyangHtml> findByTaskid(String taskid);
}
