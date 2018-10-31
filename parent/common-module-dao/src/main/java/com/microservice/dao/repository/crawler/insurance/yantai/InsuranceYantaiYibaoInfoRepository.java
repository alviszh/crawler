package com.microservice.dao.repository.crawler.insurance.yantai;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.insurance.yantai.InsuranceYantaiYibaoInfo;
/**
 * 烟台社保  Repository
 * @author qizhongbin
 *
 */
public interface InsuranceYantaiYibaoInfoRepository extends JpaRepository<InsuranceYantaiYibaoInfo, Long>{
	List<InsuranceYantaiYibaoInfo> findByTaskid(String taskid);
}
