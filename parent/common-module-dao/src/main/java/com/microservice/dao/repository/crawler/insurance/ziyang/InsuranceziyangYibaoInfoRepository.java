package com.microservice.dao.repository.crawler.insurance.ziyang;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.insurance.ziyang.InsuranceziyangYibaoInfo;
public interface InsuranceziyangYibaoInfoRepository extends JpaRepository<InsuranceziyangYibaoInfo, Long>{
	List<InsuranceziyangYibaoInfo> findByTaskid(String taskid);
}
