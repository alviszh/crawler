package com.microservice.dao.repository.crawler.insurance.binzhou;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.insurance.binzhou.InsuranceBinZhouYibaoInfo;
/**
 * 滨州社保  Repository
 * @author qizhongbin
 *
 */
public interface InsuranceBinZhouYibaoInfoRepository extends JpaRepository<InsuranceBinZhouYibaoInfo, Long>{
	List<InsuranceBinZhouYibaoInfo> findByTaskid(String taskid);
}
