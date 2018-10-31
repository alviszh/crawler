package com.microservice.dao.repository.crawler.insurance.panjin;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.insurance.panjin.InsurancePanJinChargeDetail;


/**
 * @description:
 * @author: sln 
 */
@Repository
public interface InsurancePanJinChargeDetailRepository extends JpaRepository<InsurancePanJinChargeDetail, Long> {

}
