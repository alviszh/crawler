package com.microservice.dao.repository.crawler.insurance.fuyang;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.insurance.fuyang.InsurancefuyangYibaoInfo;
/**
 * 德州社保  Repository
 * @author qizhongbin
 *
 */
public interface InsurancefuyangYibaoInfoRepository extends JpaRepository<InsurancefuyangYibaoInfo, Long>{
	List<InsurancefuyangYibaoInfo> findByTaskid(String taskid);
}
