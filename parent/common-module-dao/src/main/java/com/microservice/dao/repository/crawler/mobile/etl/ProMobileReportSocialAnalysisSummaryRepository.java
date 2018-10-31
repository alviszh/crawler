package com.microservice.dao.repository.crawler.mobile.etl;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.mobile.etl.ProMobileReportSocialAnalysisSummary;


public interface ProMobileReportSocialAnalysisSummaryRepository extends JpaRepository<ProMobileReportSocialAnalysisSummary, Long>{

	List<ProMobileReportSocialAnalysisSummary> findByTaskId(String taskid);
}
