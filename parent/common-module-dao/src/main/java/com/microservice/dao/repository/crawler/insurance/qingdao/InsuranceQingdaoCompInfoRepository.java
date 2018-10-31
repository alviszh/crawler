package com.microservice.dao.repository.crawler.insurance.qingdao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.insurance.qingdao.InsuranceQingdaoCompInfo;

public interface InsuranceQingdaoCompInfoRepository extends JpaRepository<InsuranceQingdaoCompInfo, Long> {
	List<InsuranceQingdaoCompInfo> findByTaskid(String taskid);
}
