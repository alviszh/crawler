package com.microservice.dao.repository.crawler.mobile.etl;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.mobile.etl.ProMobileReportCarrierConsume;


public interface ProMobileReportCarrierConsumeRepository extends JpaRepository<ProMobileReportCarrierConsume, Long>{

	List<ProMobileReportCarrierConsume> findByTaskId(String taskid);
}
