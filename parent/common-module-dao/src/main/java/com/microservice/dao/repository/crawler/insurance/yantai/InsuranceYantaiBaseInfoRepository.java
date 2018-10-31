package com.microservice.dao.repository.crawler.insurance.yantai;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.insurance.yantai.InsuranceYantaiBaseInfo;

/**
 * 烟台社保  Repository
 * @author qizhongbin
 *
 */
public interface InsuranceYantaiBaseInfoRepository extends JpaRepository<InsuranceYantaiBaseInfo, Long>{
	List<InsuranceYantaiBaseInfo> findByTaskid(String taskid);
}
