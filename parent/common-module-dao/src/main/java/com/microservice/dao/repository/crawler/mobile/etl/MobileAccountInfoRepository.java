package com.microservice.dao.repository.crawler.mobile.etl;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.microservice.dao.entity.crawler.mobile.etl.MobileAccountInfo;

public interface MobileAccountInfoRepository extends JpaRepository<MobileAccountInfo, Long>{

	List<MobileAccountInfo> findByTaskId(String taskid);

}
