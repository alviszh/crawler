package com.microservice.dao.repository.crawler.insurance.kaifeng;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.insurance.kaifeng.InsurancekaifengYanglaoInfo;
public interface InsurancekaifengYanglaoInfoRepository extends JpaRepository<InsurancekaifengYanglaoInfo, Long>{
	List<InsurancekaifengYanglaoInfo> findByTaskid(String taskid);
}
