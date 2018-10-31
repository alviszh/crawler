package com.microservice.dao.repository.crawler.insurance.huaian;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.insurance.huaian.InsuranceHuaiAnUnemployment;

@Repository
public interface InsuranceHuaiAnRepositoryUnemployment extends JpaRepository<InsuranceHuaiAnUnemployment,Long>{

}
