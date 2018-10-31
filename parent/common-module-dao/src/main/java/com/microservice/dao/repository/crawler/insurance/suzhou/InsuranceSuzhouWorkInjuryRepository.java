package com.microservice.dao.repository.crawler.insurance.suzhou;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.insurance.suzhou.InsuranceSuzhouUnemployment;
import com.microservice.dao.entity.crawler.insurance.suzhou.InsuranceSuzhouWorkInjury;

public interface InsuranceSuzhouWorkInjuryRepository extends JpaRepository<InsuranceSuzhouWorkInjury, Long>{

}
