package com.microservice.dao.repository.crawler.insurance.zibo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.insurance.zibo.InsuranceZiboHtml;

public interface InsuranceZiboHtmlRepository extends JpaRepository<InsuranceZiboHtml, Long>{

	List<InsuranceZiboHtml> findByTaskid(String taskid);

}
