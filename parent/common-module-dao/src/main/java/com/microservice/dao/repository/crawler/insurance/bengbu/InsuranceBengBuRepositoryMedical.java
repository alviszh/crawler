package com.microservice.dao.repository.crawler.insurance.bengbu;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.insurance.bengbu.InsuranceBengBuHtml;
import com.microservice.dao.entity.crawler.insurance.bengbu.InsuranceBengBuMedical;

@Repository
public interface InsuranceBengBuRepositoryMedical extends JpaRepository<InsuranceBengBuMedical, Long>{

}
