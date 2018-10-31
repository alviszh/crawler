package com.microservice.dao.repository.crawler.insurance.ziyang;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.insurance.ziyang.InsuranceziyangShiyeInfo;
public interface InsuranceziyangShiyeInfoRepository extends JpaRepository<InsuranceziyangShiyeInfo, Long>{
	List<InsuranceziyangShiyeInfo> findByTaskid(String taskid);
}
