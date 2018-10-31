package com.microservice.dao.repository.crawler.taxation.basic;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.taxation.basic.TaxationFlowStatus;

public interface TaxationFlowStatusRepository extends JpaRepository<TaxationFlowStatus,Long>{

}
