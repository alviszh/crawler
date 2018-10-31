package com.microservice.dao.repository.crawler.insurance.maoming;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.insurance.maoming.InsuranceMaoMingHtml;
import com.microservice.dao.entity.crawler.insurance.maoming.InsuranceMaoMingMedical;

@Repository
public interface InsuranceMaoMingRepositoryMedical extends JpaRepository<InsuranceMaoMingMedical, Long>{

}
