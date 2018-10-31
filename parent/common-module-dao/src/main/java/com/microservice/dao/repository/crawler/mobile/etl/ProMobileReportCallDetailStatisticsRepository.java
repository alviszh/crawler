package com.microservice.dao.repository.crawler.mobile.etl;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.mobile.etl.ProMobileReportCallDetailStatistics;


public interface ProMobileReportCallDetailStatisticsRepository extends JpaRepository<ProMobileReportCallDetailStatistics, Long>{

	List<ProMobileReportCallDetailStatistics> findByTaskIdAndDataTypeOrderByCommunicateDurationDesc(String taskid,String dataType);
	
	List<ProMobileReportCallDetailStatistics> findByPhonenumAndPhonenumFlagAndPhonenumType(String phonenum,String phonenumFlag,String phonenumType);
}
