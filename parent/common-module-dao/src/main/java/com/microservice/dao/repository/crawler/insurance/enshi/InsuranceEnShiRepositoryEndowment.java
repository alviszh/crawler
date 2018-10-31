package com.microservice.dao.repository.crawler.insurance.enshi;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.insurance.enshi.InsuranceEnShiEndowment;
import com.microservice.dao.entity.crawler.insurance.enshi.InsuranceEnShiHtml;

@Repository
public interface InsuranceEnShiRepositoryEndowment extends JpaRepository<InsuranceEnShiEndowment, Long>{

}
