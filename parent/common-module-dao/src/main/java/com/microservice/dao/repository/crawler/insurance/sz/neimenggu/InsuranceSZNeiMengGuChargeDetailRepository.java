package com.microservice.dao.repository.crawler.insurance.sz.neimenggu;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.insurance.sz.neimenggu.InsuranceSZNeiMengGuChargeDetail;
@Repository
public interface InsuranceSZNeiMengGuChargeDetailRepository extends JpaRepository<InsuranceSZNeiMengGuChargeDetail, Long> {

}
