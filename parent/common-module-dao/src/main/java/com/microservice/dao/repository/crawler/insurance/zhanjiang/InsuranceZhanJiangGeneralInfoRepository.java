package com.microservice.dao.repository.crawler.insurance.zhanjiang;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.insurance.zhanjiang.InsuranceZhanJiangGeneralInfo;

public interface InsuranceZhanJiangGeneralInfoRepository extends JpaRepository<InsuranceZhanJiangGeneralInfo, Long>{

	List<InsuranceZhanJiangGeneralInfo> findByTaskid(String taskid);
}
