/**
 * 
 */
package com.microservice.dao.repository.crawler.insurance.daqing;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.insurance.daqing.InsuranceDaQingPension;

@Repository
public interface InsuranceDaQingPensionRepository extends JpaRepository<InsuranceDaQingPension, Long> {

}
