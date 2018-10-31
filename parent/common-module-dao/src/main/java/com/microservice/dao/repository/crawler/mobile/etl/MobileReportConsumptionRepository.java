package com.microservice.dao.repository.crawler.mobile.etl;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.mobile.etl.MobileReportConsumption;

public interface MobileReportConsumptionRepository extends JpaRepository<MobileReportConsumption, Long>{
	
	List<MobileReportConsumption> findByTaskId(String taskid);

}
