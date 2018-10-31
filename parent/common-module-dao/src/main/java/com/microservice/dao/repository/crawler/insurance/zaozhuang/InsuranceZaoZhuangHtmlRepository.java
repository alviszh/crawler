package com.microservice.dao.repository.crawler.insurance.zaozhuang;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.insurance.zaozhuang.InsuranceZaoZhuangHtml;

public interface InsuranceZaoZhuangHtmlRepository extends JpaRepository<InsuranceZaoZhuangHtml, Long>{
	List<InsuranceZaoZhuangHtml> findByTaskid(String taskid);
}
