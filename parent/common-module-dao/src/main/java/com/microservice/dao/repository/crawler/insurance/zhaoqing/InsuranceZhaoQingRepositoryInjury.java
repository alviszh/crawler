package com.microservice.dao.repository.crawler.insurance.zhaoqing;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.insurance.zhaoqing.InsuranceZhaoQingHtml;
import com.microservice.dao.entity.crawler.insurance.zhaoqing.InsuranceZhaoQingInjury;

@Repository
public interface InsuranceZhaoQingRepositoryInjury extends JpaRepository<InsuranceZhaoQingInjury, Long>{

}
