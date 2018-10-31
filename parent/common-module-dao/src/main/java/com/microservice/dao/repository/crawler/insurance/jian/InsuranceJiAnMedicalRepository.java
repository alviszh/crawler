package com.microservice.dao.repository.crawler.insurance.jian;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.insurance.huzhou.InsuranceHuzhouBasicinfo;
import com.microservice.dao.entity.crawler.insurance.jian.InsuranceJiAnMedical;

public interface InsuranceJiAnMedicalRepository extends JpaRepository<InsuranceJiAnMedical, Long>{

}
