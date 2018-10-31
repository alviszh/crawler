package com.microservice.dao.repository.crawler.mobile.etl;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.mobile.etl.MobilePaymentInfo;

public interface MobilePaymentInfoRepository extends JpaRepository<MobilePaymentInfo, Long>{

	List<MobilePaymentInfo> findByTaskId(String taskid);

}
