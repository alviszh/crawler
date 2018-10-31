package com.microservice.dao.repository.crawler.insurance.shaoxing;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.insurance.shaoxing.InsuranceShaoXingUserInfo;

@Repository
public interface InsuranceShaoXingRepositoryUserInfo extends JpaRepository<InsuranceShaoXingUserInfo,Long>{

}
