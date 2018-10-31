package com.microservice.dao.repository.crawler.insurance.fuyang;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.insurance.fuyang.InsurancefuyangBaseInfo;

/**
 * 德州社保  Repository
 * @author qizhongbin
 *
 */
public interface InsurancefuyangBaseInfoRepository extends JpaRepository<InsurancefuyangBaseInfo, Long>{
	List<InsurancefuyangBaseInfo> findByTaskid(String taskid);
}
