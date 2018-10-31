package com.microservice.dao.repository.crawler.insurance.shaoguan;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.insurance.shaoguan.InsuranceShaoGuanHtml;
import com.microservice.dao.entity.crawler.insurance.shaoguan.InsuranceShaoGuanUserInfo;

@Repository
public interface InsuranceShaoGuanRepositoryUserInfo extends JpaRepository<InsuranceShaoGuanUserInfo, Long>{

}
