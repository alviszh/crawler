package com.microservice.dao.repository.crawler.mobile.etl;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.mobile.etl.MobileGroupNetInfo;

public interface MobileGroupNetInfoRepository extends JpaRepository<MobileGroupNetInfo, Long>{

	List<MobileGroupNetInfo> findByTaskId(String taskid);

}
