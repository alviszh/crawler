package com.microservice.dao.repository.crawler.insurance.ziyang;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.insurance.ziyang.InsuranceziyangBaseInfo;

public interface InsuranceziyangBaseInfoRepository extends JpaRepository<InsuranceziyangBaseInfo, Long>{
	List<InsuranceziyangBaseInfo> findByTaskid(String taskid);
}
