package com.microservice.dao.repository.crawler.insurance.nanning;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.insurance.nanning.InsuranceNanNingMedical;

@Repository
public interface InsuranceNanNingRepositoryMedical extends JpaRepository<InsuranceNanNingMedical,Long>{

}
