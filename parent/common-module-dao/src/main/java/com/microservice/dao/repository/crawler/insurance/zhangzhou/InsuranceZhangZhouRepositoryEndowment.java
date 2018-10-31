package com.microservice.dao.repository.crawler.insurance.zhangzhou;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.insurance.zhangzhou.InsuranceZhangZhouEndowment;
import com.microservice.dao.entity.crawler.insurance.zhangzhou.InsuranceZhangZhouHtml;

@Repository
public interface InsuranceZhangZhouRepositoryEndowment extends JpaRepository<InsuranceZhangZhouEndowment, Long>{

}
