package com.microservice.dao.repository.crawler.housing.luohe;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.housing.luohe.HousingLuoheUserInfo;

public interface HousingLuoheUserInfoRepository extends JpaRepository<HousingLuoheUserInfo, Long>{
	
}
