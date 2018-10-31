package com.microservice.dao.repository.crawler.mobile.etl;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.mobile.etl.ProMobilePayInfo;

public interface ProMobilePayInfoRepository extends JpaRepository<ProMobilePayInfo, Long>{

	List<ProMobilePayInfo> findByTaskId(String taskid);
}
