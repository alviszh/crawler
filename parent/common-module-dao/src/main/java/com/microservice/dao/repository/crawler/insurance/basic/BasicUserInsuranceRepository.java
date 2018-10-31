package com.microservice.dao.repository.crawler.insurance.basic;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.insurance.basic.BasicUserInsurance;

public interface BasicUserInsuranceRepository extends JpaRepository<BasicUserInsurance, Long>{

	BasicUserInsurance findByNameAndIdnum(String username, String idnum);
	
	BasicUserInsurance findTopByIdnumOrderByCreatetimeDesc(String idnum);

}
