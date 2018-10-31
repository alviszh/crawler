package com.microservice.dao.repository.crawler.insurance.dezhou;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.insurance.dezhou.InsuranceDeZhouYibaoInfo;
/**
 * 德州社保  Repository
 * @author qizhongbin
 *
 */
public interface InsuranceDeZhouYibaoInfoRepository extends JpaRepository<InsuranceDeZhouYibaoInfo, Long>{
	List<InsuranceDeZhouYibaoInfo> findByTaskid(String taskid);
}
