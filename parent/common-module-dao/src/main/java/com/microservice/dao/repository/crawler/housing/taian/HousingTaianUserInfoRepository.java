package com.microservice.dao.repository.crawler.housing.taian;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.housing.taian.HousingTaianUserInfo;

public interface HousingTaianUserInfoRepository extends JpaRepository<HousingTaianUserInfo, Long>{
	
}
