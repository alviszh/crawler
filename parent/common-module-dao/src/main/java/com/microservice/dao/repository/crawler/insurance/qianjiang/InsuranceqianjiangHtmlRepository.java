package com.microservice.dao.repository.crawler.insurance.qianjiang;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.insurance.qianjiang.InsuranceqianjiangHtml;

public interface InsuranceqianjiangHtmlRepository extends JpaRepository<InsuranceqianjiangHtml, Long>{
	List<InsuranceqianjiangHtml> findByTaskid(String taskid);
}
