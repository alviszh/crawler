package com.microservice.dao.repository.crawler.housing.shanghai;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.housing.shanghai.HousingShanghaiUserInfo;

public interface HousingShanghaiUserInfoRepository extends JpaRepository<HousingShanghaiUserInfo, Long>{
	
}
