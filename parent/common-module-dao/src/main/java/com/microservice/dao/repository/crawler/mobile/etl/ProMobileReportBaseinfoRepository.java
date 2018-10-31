package com.microservice.dao.repository.crawler.mobile.etl;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.mobile.etl.ProMobileReportBaseinfo;


public interface ProMobileReportBaseinfoRepository extends JpaRepository<ProMobileReportBaseinfo, Long>{

	List<ProMobileReportBaseinfo> findByTaskId(String taskid);
}
