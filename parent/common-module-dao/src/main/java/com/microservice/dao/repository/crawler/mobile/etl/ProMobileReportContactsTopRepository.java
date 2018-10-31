package com.microservice.dao.repository.crawler.mobile.etl;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.mobile.etl.ProMobileReportContactsTop;


public interface ProMobileReportContactsTopRepository extends JpaRepository<ProMobileReportContactsTop, Long>{

	List<ProMobileReportContactsTop> findByTaskId(String taskid);
}
