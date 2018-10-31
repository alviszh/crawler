package com.microservice.dao.repository.crawler.insurance.nanning;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.insurance.nanning.InsuranceNanNingUnemployment;

@Repository
public interface InsuranceNanNingRepositoryUnemployment extends JpaRepository<InsuranceNanNingUnemployment,Long>{

}
