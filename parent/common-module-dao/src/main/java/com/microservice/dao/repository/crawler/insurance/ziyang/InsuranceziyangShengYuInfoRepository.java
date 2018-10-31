package com.microservice.dao.repository.crawler.insurance.ziyang;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.insurance.ziyang.InsuranceziyangShengYuInfo;
public interface InsuranceziyangShengYuInfoRepository extends JpaRepository<InsuranceziyangShengYuInfo, Long>{
	List<InsuranceziyangShengYuInfo> findByTaskid(String taskid);
}
