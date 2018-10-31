package com.microservice.dao.repository.crawler.insurance.guiyang;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.insurance.guiyang.InsuranceGuiyangBuChongYibaoInfo;
/**
 * 贵阳社保  Repository
 * @author qizhongbin
 *
 */
public interface InsuranceGuiyangBuchongYanglaoInfoRepository extends JpaRepository<InsuranceGuiyangBuChongYibaoInfo, Long>{
	List<InsuranceGuiyangBuChongYibaoInfo> findByTaskid(String taskid);
}
