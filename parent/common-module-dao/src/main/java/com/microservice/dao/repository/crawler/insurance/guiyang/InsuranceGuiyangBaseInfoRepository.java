package com.microservice.dao.repository.crawler.insurance.guiyang;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.insurance.guiyang.InsuranceGuiyangBaseInfo;

/**
 * 贵阳社保  Repository
 * @author qizhongbin
 *
 */
public interface InsuranceGuiyangBaseInfoRepository extends JpaRepository<InsuranceGuiyangBaseInfo, Long>{
	List<InsuranceGuiyangBaseInfo> findByTaskid(String taskid);
}
