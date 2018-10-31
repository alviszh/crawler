package com.microservice.dao.repository.crawler.insurance.nantong;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.insurance.nantong.InsuranceNanTongHtml;

public interface InsuranceNanTongHtmlRepository extends JpaRepository<InsuranceNanTongHtml, Long>{
	List<InsuranceNanTongHtml> findByTaskid(String taskid);
}
