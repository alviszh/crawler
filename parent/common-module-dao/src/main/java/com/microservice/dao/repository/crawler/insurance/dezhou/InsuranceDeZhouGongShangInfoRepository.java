package com.microservice.dao.repository.crawler.insurance.dezhou;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.insurance.dezhou.InsuranceDeZhouGongShangInfo;
/**
 * 德州社保  Repository
 * @author qizhongbin
 *
 */
public interface InsuranceDeZhouGongShangInfoRepository extends JpaRepository<InsuranceDeZhouGongShangInfo, Long>{
	List<InsuranceDeZhouGongShangInfo> findByTaskid(String taskid);
}
