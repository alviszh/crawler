package com.microservice.dao.repository.crawler.insurance.maoming;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.insurance.maoming.InsuranceMaoMingEndowment;
import com.microservice.dao.entity.crawler.insurance.maoming.InsuranceMaoMingHtml;

@Repository
public interface InsuranceMaoMingRepositoryEndowment extends JpaRepository<InsuranceMaoMingEndowment, Long>{

}
