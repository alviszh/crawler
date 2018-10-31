package com.microservice.dao.repository.crawler.insurance.ningbo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.insurance.ningbo.InsuranceNingboLost;

@Repository
public interface InsuranceNingboLostRepository extends JpaRepository<InsuranceNingboLost,Long>{

}
