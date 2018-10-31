package com.microservice.dao.repository.crawler.insurance.shanghai;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.insurance.shanghai.InsuranceShanghaiHtml;

public interface InsuranceShanghaiHtmlRepository extends JpaRepository<InsuranceShanghaiHtml, Long>{
	List<InsuranceShanghaiHtml> findByTaskid(String taskid);
}
