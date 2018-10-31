package com.microservice.dao.repository.crawler.insurance.sz.xinjiang;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.insurance.sz.xinjiang.InsuranceXinJiangInfo;
public interface InsuranceXinJiangInfoRepository extends JpaRepository<InsuranceXinJiangInfo, Long>{
	List<InsuranceXinJiangInfo> findByTaskid(String taskid);
}
