package com.microservice.dao.repository.crawler.insurance.sz.xinjiang;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.insurance.sz.xinjiang.InsuranceXinJiangHtml;

public interface InsuranceXinJiangHtmlRepository extends JpaRepository<InsuranceXinJiangHtml, Long>{
	List<InsuranceXinJiangHtml> findByTaskid(String taskid);
}
