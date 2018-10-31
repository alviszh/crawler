package com.microservice.dao.repository.crawler.insurance.mianyang;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.insurance.mianyang.InsurancemianyangYibaoInfo;
public interface InsurancemianyangYibaoInfoRepository extends JpaRepository<InsurancemianyangYibaoInfo, Long>{
	List<InsurancemianyangYibaoInfo> findByTaskid(String taskid);
}
