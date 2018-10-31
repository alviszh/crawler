package com.microservice.dao.repository.crawler.housing.etl;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.housing.etl.HousingUserInfo;

public interface HousingUserInfoRepository extends JpaRepository<HousingUserInfo, Long>{

	List<HousingUserInfo> findByTaskId(String taskid);

}
