package com.microservice.dao.repository.crawler.housing.tongyi.fuyang;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.housing.tongyi.fuyang.HousingAnHuiTongyiFuYangUserInfo;

@Repository
public interface HousingAnHuiTongyiFuYangUserInfoRepository extends JpaRepository<HousingAnHuiTongyiFuYangUserInfo, Long>{
	
}
