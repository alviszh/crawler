package com.microservice.dao.repository.crawler.insurance.enshi;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.insurance.enshi.InsuranceEnShiHtml;
import com.microservice.dao.entity.crawler.insurance.enshi.InsuranceEnShiInjury;

@Repository
public interface InsuranceEnShiRepositoryInjury extends JpaRepository<InsuranceEnShiInjury, Long>{

}
