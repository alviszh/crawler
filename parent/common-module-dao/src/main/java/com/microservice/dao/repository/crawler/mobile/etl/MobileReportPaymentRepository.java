package com.microservice.dao.repository.crawler.mobile.etl;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.mobile.etl.MobileReportPayment;

public interface MobileReportPaymentRepository extends JpaRepository<MobileReportPayment, Long>{
	
	List<MobileReportPayment> findByTaskId(String taskid);

}
