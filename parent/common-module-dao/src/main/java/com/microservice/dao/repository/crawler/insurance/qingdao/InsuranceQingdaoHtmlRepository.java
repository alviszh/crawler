package com.microservice.dao.repository.crawler.insurance.qingdao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.insurance.qingdao.InsuranceQingdaoHtml;

public interface InsuranceQingdaoHtmlRepository extends JpaRepository<InsuranceQingdaoHtml, Long> {
	List<InsuranceQingdaoHtml> findByTaskid(String taskid);
}
