package com.microservice.dao.repository.crawler.insurance.ganzhou;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.insurance.ganzhou.InsuranceGanZhouUserInfo;

public interface InsuranceGanZhouUserInfoRepository extends JpaRepository<InsuranceGanZhouUserInfo, Long>{

	InsuranceGanZhouUserInfo findTopByTaskid(String taskId);

}
