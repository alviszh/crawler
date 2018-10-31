package com.microservice.dao.repository.crawler.insurance.yantai;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.insurance.yantai.InsuranceYantaiShengyuInfo;
/**
 * 烟台社保  Repository
 * @author qizhongbin
 *
 */
public interface InsuranceYantaiShengyuInfoRepository extends JpaRepository<InsuranceYantaiShengyuInfo, Long>{
	List<InsuranceYantaiShengyuInfo> findByTaskid(String taskid);
}
