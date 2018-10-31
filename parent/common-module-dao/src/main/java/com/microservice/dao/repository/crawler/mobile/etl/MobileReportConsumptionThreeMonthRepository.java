package com.microservice.dao.repository.crawler.mobile.etl;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.mobile.etl.MobileReportConsumptionThreeMonth;

public interface MobileReportConsumptionThreeMonthRepository extends JpaRepository<MobileReportConsumptionThreeMonth, Long>{
	
	List<MobileReportConsumptionThreeMonth> findByTaskId(String taskid);

}
