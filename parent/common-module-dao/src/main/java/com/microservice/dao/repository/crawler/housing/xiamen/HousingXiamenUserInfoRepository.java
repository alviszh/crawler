package com.microservice.dao.repository.crawler.housing.xiamen;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.housing.xiamen.HousingXiamenUserInfo;

public interface HousingXiamenUserInfoRepository extends JpaRepository<HousingXiamenUserInfo, Long>{

	HousingXiamenUserInfo findTopByTaskid(String taskId);
}
