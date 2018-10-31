package com.microservice.dao.repository.crawler.insurance.guiyang;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.insurance.guiyang.InsuranceGuiyangJiGuanYanglaoInfo;
/**
 * 贵阳社保  Repository
 * @author qizhongbin
 *
 */
public interface InsuranceGuiyangJiGuanYanglaoInfoRepository extends JpaRepository<InsuranceGuiyangJiGuanYanglaoInfo, Long>{
	List<InsuranceGuiyangJiGuanYanglaoInfo> findByTaskid(String taskid);
}
