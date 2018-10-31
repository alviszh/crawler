package com.microservice.dao.repository.crawler.insurance.ziyang;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.insurance.ziyang.InsuranceziyangGongShangInfo;
public interface InsuranceziyangGongShangInfoRepository extends JpaRepository<InsuranceziyangGongShangInfo, Long>{
	List<InsuranceziyangGongShangInfo> findByTaskid(String taskid);
}
