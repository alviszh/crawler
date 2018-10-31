package com.microservice.dao.repository.crawler.mobile.etl;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.mobile.etl.MobileReportConsumptionSixMonth;

public interface MobileReportConsumptionSixMonthRepository extends JpaRepository<MobileReportConsumptionSixMonth, Long>{
	
	List<MobileReportConsumptionSixMonth> findByTaskId(String taskid);

}
