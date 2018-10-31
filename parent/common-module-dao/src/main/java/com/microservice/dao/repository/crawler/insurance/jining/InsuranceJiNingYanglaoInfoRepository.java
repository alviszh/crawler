package com.microservice.dao.repository.crawler.insurance.jining;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.insurance.jining.InsuranceJiNingYanglaoInfo;
public interface InsuranceJiNingYanglaoInfoRepository extends JpaRepository<InsuranceJiNingYanglaoInfo, Long>{
	List<InsuranceJiNingYanglaoInfo> findByTaskid(String taskid);
}
