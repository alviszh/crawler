package com.microservice.dao.repository.crawler.insurance.jingzhou;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.insurance.jingzhou.InsurancejingzhouHtml;

public interface InsurancejingzhouHtmlRepository extends JpaRepository<InsurancejingzhouHtml, Long>{
//	List<InsurancejingzhouHtml> findByTaskid(String taskid);
}
