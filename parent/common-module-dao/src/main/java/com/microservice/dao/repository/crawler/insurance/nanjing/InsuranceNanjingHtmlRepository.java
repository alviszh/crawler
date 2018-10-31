package com.microservice.dao.repository.crawler.insurance.nanjing;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.insurance.nanjing.InsuranceNanjingHtml;

/**
 * @description: 南京社保html存储
 * @author: sln 
 * @date: 2017年9月26日 下午6:42:56 
 */
@Repository
public interface InsuranceNanjingHtmlRepository extends JpaRepository<InsuranceNanjingHtml, Long> {

}
