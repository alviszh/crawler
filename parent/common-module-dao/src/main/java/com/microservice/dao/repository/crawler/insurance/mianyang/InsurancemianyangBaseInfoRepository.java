package com.microservice.dao.repository.crawler.insurance.mianyang;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.insurance.mianyang.InsurancemianyangBaseInfo;

public interface InsurancemianyangBaseInfoRepository extends JpaRepository<InsurancemianyangBaseInfo, Long>{
	List<InsurancemianyangBaseInfo> findByTaskid(String taskid);
}
