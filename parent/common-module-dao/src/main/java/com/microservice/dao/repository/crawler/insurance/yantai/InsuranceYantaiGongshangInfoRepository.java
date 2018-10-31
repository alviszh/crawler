package com.microservice.dao.repository.crawler.insurance.yantai;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.insurance.yantai.InsuranceYantaiGongshangInfo;
/**
 * 烟台社保  Repository
 * @author qizhongbin
 *
 */
public interface InsuranceYantaiGongshangInfoRepository extends JpaRepository<InsuranceYantaiGongshangInfo, Long>{
	List<InsuranceYantaiGongshangInfo> findByTaskid(String taskid);
}
