package com.microservice.dao.repository.crawler.insurance.nanjing;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.insurance.nanjing.InsuranceNanjingUserInfo;

/**
 * @description: 南京社保用户信息存储
 * @author: sln 
 * @date: 2017年9月26日 下午6:45:11 
 */
@Repository
public interface InsuranceNanjingUserInfoRepository extends JpaRepository<InsuranceNanjingUserInfo, Long> {

}
