package com.microservice.dao.repository.crawler.insurance.binzhou;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.insurance.binzhou.InsuranceBinZhouYanglaoInfo;
/**
 * 滨州社保  Repository
 * @author qizhongbin
 *
 */
public interface InsuranceBinZhouYanglaoInfoRepository extends JpaRepository<InsuranceBinZhouYanglaoInfo, Long>{
	List<InsuranceBinZhouYanglaoInfo> findByTaskid(String taskid);
}
