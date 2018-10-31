package com.microservice.dao.repository.crawler.insurance.shaoxing;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.insurance.shaoxing.InsuranceShaoXingUnemployment;

@Repository
public interface InsuranceShaoXingRepositoryUnemployment extends JpaRepository<InsuranceShaoXingUnemployment,Long>{

}
