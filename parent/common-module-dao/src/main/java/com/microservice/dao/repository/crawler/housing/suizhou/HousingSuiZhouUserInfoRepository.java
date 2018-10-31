package com.microservice.dao.repository.crawler.housing.suizhou;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.housing.suizhou.HousingSuiZhouUserInfo;

public interface HousingSuiZhouUserInfoRepository extends JpaRepository<HousingSuiZhouUserInfo, Long>{

	HousingSuiZhouUserInfo findTopByTaskid(String taskId);
}
