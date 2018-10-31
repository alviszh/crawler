package com.microservice.dao.repository.crawler.insurance.nanyang;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.insurance.nanyang.InsuranceNanYangUserInfo;

@Repository
public interface InsuranceNanYangRepositoryUserInfo extends JpaRepository<InsuranceNanYangUserInfo,Long>{

}
