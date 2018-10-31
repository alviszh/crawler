package com.microservice.dao.repository.crawler.mobile.etl;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.mobile.etl.MobileSmsRecordDetail;

public interface MobileSmsRecordDetailRepository extends JpaRepository<MobileSmsRecordDetail, Long>{

	List<MobileSmsRecordDetail> findByTaskId(String taskid);

}
