package com.microservice.dao.repository.crawler.insurance.ziyang;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.insurance.ziyang.InsuranceziyangYanglaoInfo;
public interface InsuranceziyangYanglaoInfoRepository extends JpaRepository<InsuranceziyangYanglaoInfo, Long>{
	List<InsuranceziyangYanglaoInfo> findByTaskid(String taskid);
}
