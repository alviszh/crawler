package com.microservice.dao.repository.crawler.insurance.pingdingshan;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.insurance.pingdingshan.InsurancePingDingShanEndowment;

@Repository
public interface InsurancePingDingShanRepositoryEndowment extends JpaRepository<InsurancePingDingShanEndowment,Long>{

}
