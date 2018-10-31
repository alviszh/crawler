package com.microservice.dao.repository.crawler.insurance.weihai;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.insurance.weihai.InsuranceWeiHaiHtml;

public interface InsuranceWeiHaiHtmlRepository extends JpaRepository<InsuranceWeiHaiHtml, Long>{
	List<InsuranceWeiHaiHtml> findByTaskid(String taskid);
}
