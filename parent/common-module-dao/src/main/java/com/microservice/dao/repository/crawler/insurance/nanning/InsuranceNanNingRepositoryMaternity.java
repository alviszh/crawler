package com.microservice.dao.repository.crawler.insurance.nanning;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.insurance.nanning.InsuranceNanNingMaternity;

@Repository
public interface InsuranceNanNingRepositoryMaternity extends JpaRepository<InsuranceNanNingMaternity,Long>{

}
