package com.microservice.dao.repository.crawler.insurance.zhangzhou;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.insurance.zhangzhou.InsuranceZhangZhouHtml;
import com.microservice.dao.entity.crawler.insurance.zhangzhou.InsuranceZhangZhouUnemployment;

@Repository
public interface InsuranceZhangZhouRepositoryUnemployment extends JpaRepository<InsuranceZhangZhouUnemployment, Long>{

}
