package com.microservice.dao.repository.crawler.mobile.etl;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.mobile.etl.ProMobileReportConsumeinfo;


public interface ProMobileReportConsumeinfoRepository extends JpaRepository<ProMobileReportConsumeinfo, Long>{

	List<ProMobileReportConsumeinfo> findByTaskId(String taskid);
}
