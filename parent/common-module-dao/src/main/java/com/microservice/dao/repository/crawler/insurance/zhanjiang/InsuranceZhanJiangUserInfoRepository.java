package com.microservice.dao.repository.crawler.insurance.zhanjiang;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.insurance.zhanjiang.InsuranceZhanJiangUserInfo;

public interface InsuranceZhanJiangUserInfoRepository extends JpaRepository<InsuranceZhanJiangUserInfo, Long>{

	List<InsuranceZhanJiangUserInfo> findByTaskid(String taskid);
}
