package com.microservice.dao.repository.crawler.mobile.etl;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.mobile.etl.MobileBusinessInfo;

public interface MobileBusinessInfoRepository extends JpaRepository<MobileBusinessInfo, Long>{

	List<MobileBusinessInfo> findByTaskId(String taskid);

}
