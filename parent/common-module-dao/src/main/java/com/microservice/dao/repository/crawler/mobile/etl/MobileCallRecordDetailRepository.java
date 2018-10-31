package com.microservice.dao.repository.crawler.mobile.etl;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.microservice.dao.entity.crawler.mobile.etl.MobileCallRecordDetail;

public interface MobileCallRecordDetailRepository extends JpaRepository<MobileCallRecordDetail, Long>{

	List<MobileCallRecordDetail> findByTaskId(String taskid);
	
	@Query(value="select count(*) from MobileCallRecordDetail where taskId =?1")
	int countEltTreatResultByTaskId(String taskId);
	
}
