package com.microservice.dao.repository.crawler.taxation.basic;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.taxation.basic.AreaCode;


public interface AreaCodeRepository extends JpaRepository<AreaCode, Long>{

//	List<AreaCode> findByIsTaxationFinishedLessThan(int i);
}
