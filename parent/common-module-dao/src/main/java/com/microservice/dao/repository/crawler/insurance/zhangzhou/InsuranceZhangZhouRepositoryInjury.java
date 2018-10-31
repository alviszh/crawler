package com.microservice.dao.repository.crawler.insurance.zhangzhou;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.insurance.zhangzhou.InsuranceZhangZhouHtml;
import com.microservice.dao.entity.crawler.insurance.zhangzhou.InsuranceZhangZhouInjury;

@Repository
public interface InsuranceZhangZhouRepositoryInjury extends JpaRepository<InsuranceZhangZhouInjury, Long>{

}
