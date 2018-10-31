package com.microservice.dao.repository.crawler.insurance.mianyang;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.insurance.mianyang.InsurancemianyangGongShangInfo;
public interface InsurancemianyangGongShangInfoRepository extends JpaRepository<InsurancemianyangGongShangInfo, Long>{
	List<InsurancemianyangGongShangInfo> findByTaskid(String taskid);
}
