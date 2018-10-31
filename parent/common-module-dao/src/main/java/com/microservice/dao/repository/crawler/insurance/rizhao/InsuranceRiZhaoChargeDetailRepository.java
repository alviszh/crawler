package com.microservice.dao.repository.crawler.insurance.rizhao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.insurance.rizhao.InsuranceRiZhaoChargeDetail;

@Repository
public interface InsuranceRiZhaoChargeDetailRepository extends JpaRepository<InsuranceRiZhaoChargeDetail, Long> {

}
