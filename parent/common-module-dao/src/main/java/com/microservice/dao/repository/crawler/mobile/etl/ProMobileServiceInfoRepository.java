package com.microservice.dao.repository.crawler.mobile.etl;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.mobile.etl.ProMobileServiceInfo;

public interface ProMobileServiceInfoRepository extends JpaRepository<ProMobileServiceInfo, Long>{

	List<ProMobileServiceInfo> findByTaskId(String taskid);
}
