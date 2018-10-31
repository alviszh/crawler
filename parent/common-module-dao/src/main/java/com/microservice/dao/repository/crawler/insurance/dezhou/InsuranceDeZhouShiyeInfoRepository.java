package com.microservice.dao.repository.crawler.insurance.dezhou;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.insurance.dezhou.InsuranceDeZhouShiyeInfo;
/**
 * 德州社保  Repository
 * @author qizhongbin
 *
 */
public interface InsuranceDeZhouShiyeInfoRepository extends JpaRepository<InsuranceDeZhouShiyeInfo, Long>{
	List<InsuranceDeZhouShiyeInfo> findByTaskid(String taskid);
}
