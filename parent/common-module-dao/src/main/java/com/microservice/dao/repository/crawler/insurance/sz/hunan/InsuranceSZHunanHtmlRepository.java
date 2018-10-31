package com.microservice.dao.repository.crawler.insurance.sz.hunan;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.insurance.sz.hunan.InsuranceSZHunanHtml;

public interface InsuranceSZHunanHtmlRepository extends JpaRepository<InsuranceSZHunanHtml, Long>{

	List<InsuranceSZHunanHtml> findByTaskid(String taskid);

}
