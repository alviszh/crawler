package com.microservice.dao.repository.crawler.insurance.binzhou;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.insurance.binzhou.InsuranceBinZhouBaseInfo;

/**
 * 滨州社保  Repository
 * @author qizhongbin
 *
 */
public interface InsuranceBinZhouBaseInfoRepository extends JpaRepository<InsuranceBinZhouBaseInfo, Long>{
	List<InsuranceBinZhouBaseInfo> findByTaskid(String taskid);
}
