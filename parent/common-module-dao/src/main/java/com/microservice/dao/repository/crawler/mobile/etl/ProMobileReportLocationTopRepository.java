package com.microservice.dao.repository.crawler.mobile.etl;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.mobile.etl.ProMobileReportLocationTop;


public interface ProMobileReportLocationTopRepository extends JpaRepository<ProMobileReportLocationTop, Long>{

	List<ProMobileReportLocationTop> findByTaskId(String taskid);
}
