package com.microservice.dao.repository.crawler.taxation.basic;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.mobile.BasicUser;
import com.microservice.dao.entity.crawler.taxation.basic.BasicUserTaxation;

public interface BasicUserTaxationRepository extends JpaRepository<BasicUserTaxation, Long>{

	BasicUserTaxation findByNameAndIdnum(String username, String idnum);
	
	BasicUserTaxation findTopByIdnumOrderByCreatetimeDesc(String idnum);
}
