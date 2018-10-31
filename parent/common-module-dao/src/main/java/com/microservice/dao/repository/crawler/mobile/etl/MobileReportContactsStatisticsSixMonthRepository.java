package com.microservice.dao.repository.crawler.mobile.etl;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.mobile.etl.MobileReportContactsStatisticsSixMonth;

public interface MobileReportContactsStatisticsSixMonthRepository extends JpaRepository<MobileReportContactsStatisticsSixMonth, Long>{

	List<MobileReportContactsStatisticsSixMonth> findByTaskId(String taskid);
}
