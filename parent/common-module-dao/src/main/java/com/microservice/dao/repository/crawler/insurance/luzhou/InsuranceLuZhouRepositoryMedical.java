package com.microservice.dao.repository.crawler.insurance.luzhou;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.insurance.luzhou.InsuranceLuZhouMedical;
@Repository
public interface InsuranceLuZhouRepositoryMedical extends JpaRepository<InsuranceLuZhouMedical, Long>{

}
