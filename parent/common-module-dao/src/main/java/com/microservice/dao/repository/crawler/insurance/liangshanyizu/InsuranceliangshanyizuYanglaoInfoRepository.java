package com.microservice.dao.repository.crawler.insurance.liangshanyizu;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.insurance.liangshanyizu.InsuranceliangshanyizuYanglaoInfo;
public interface InsuranceliangshanyizuYanglaoInfoRepository extends JpaRepository<InsuranceliangshanyizuYanglaoInfo, Long>{
	List<InsuranceliangshanyizuYanglaoInfo> findByTaskid(String taskid);
}
