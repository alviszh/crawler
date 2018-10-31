package com.microservice.dao.repository.crawler.insurance.mianyang;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.insurance.mianyang.InsurancemianyangYanglaoInfo;
public interface InsurancemianyangYanglaoInfoRepository extends JpaRepository<InsurancemianyangYanglaoInfo, Long>{
	List<InsurancemianyangYanglaoInfo> findByTaskid(String taskid);
}
