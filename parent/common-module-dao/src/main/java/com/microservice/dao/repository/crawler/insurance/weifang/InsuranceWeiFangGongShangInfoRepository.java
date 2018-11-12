package com.microservice.dao.repository.crawler.insurance.weifang;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.insurance.weifang.InsuranceWeiFangGongShangInfo;
/**
 * 潍坊社保  Repository
 * @author qizhongbin
 *
 */
public interface InsuranceWeiFangGongShangInfoRepository extends JpaRepository<InsuranceWeiFangGongShangInfo, Long>{
	List<InsuranceWeiFangGongShangInfo> findByTaskid(String taskid);
}
