package com.microservice.dao.repository.crawler.insurance.guiyang;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.insurance.guiyang.InsuranceGuiyangJuminYanglaoInfo;
/**
 * 贵阳社保  Repository
 * @author qizhongbin
 *
 */
public interface InsuranceGuiyangJuminYanglaoInfoRepository extends JpaRepository<InsuranceGuiyangJuminYanglaoInfo, Long>{
	List<InsuranceGuiyangJuminYanglaoInfo> findByTaskid(String taskid);
}
