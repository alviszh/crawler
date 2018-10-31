package com.microservice.dao.repository.crawler.insurance.yantai;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.insurance.yantai.InsuranceYantaiShiyeInfo;
/**
 * 烟台社保  Repository
 * @author qizhongbin
 *
 */
public interface InsuranceYantaiShiyeInfoRepository extends JpaRepository<InsuranceYantaiShiyeInfo, Long>{
	List<InsuranceYantaiShiyeInfo> findByTaskid(String taskid);
}
