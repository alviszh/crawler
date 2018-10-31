package com.microservice.dao.repository.crawler.insurance.zhanjiang;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.insurance.zhanjiang.InsuranceZhanJiangHtml;

public interface InsuranceZhanJiangHtmlRepository extends JpaRepository<InsuranceZhanJiangHtml, Long>{

	List<InsuranceZhanJiangHtml> findByTaskid(String taskid);
}
