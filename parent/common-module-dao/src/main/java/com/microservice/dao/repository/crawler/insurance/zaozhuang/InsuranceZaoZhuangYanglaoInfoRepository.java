package com.microservice.dao.repository.crawler.insurance.zaozhuang;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.insurance.zaozhuang.InsuranceZaoZhuangYanglaoInfo;
public interface InsuranceZaoZhuangYanglaoInfoRepository extends JpaRepository<InsuranceZaoZhuangYanglaoInfo, Long>{
	List<InsuranceZaoZhuangYanglaoInfo> findByTaskid(String taskid);
}
