package com.microservice.dao.repository.crawler.insurance.yantai;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.insurance.yantai.InsuranceYantaiYanglaoInfo;
/**
 * 烟台社保  Repository
 * @author qizhongbin
 *
 */
public interface InsuranceYantaiYanglaoInfoRepository extends JpaRepository<InsuranceYantaiYanglaoInfo, Long>{
	List<InsuranceYantaiYanglaoInfo> findByTaskid(String taskid);
}
