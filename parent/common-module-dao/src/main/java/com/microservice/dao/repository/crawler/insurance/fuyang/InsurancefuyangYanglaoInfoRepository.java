package com.microservice.dao.repository.crawler.insurance.fuyang;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.insurance.fuyang.InsurancefuyangYanglaoInfo;
/**
 * 德州社保  Repository
 * @author qizhongbin
 *
 */
public interface InsurancefuyangYanglaoInfoRepository extends JpaRepository<InsurancefuyangYanglaoInfo, Long>{
	List<InsurancefuyangYanglaoInfo> findByTaskid(String taskid);
}
