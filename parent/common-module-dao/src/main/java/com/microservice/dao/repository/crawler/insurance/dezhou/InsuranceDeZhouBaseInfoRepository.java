package com.microservice.dao.repository.crawler.insurance.dezhou;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.insurance.dezhou.InsuranceDeZhouBaseInfo;

/**
 * 德州社保  Repository
 * @author qizhongbin
 *
 */
public interface InsuranceDeZhouBaseInfoRepository extends JpaRepository<InsuranceDeZhouBaseInfo, Long>{
	List<InsuranceDeZhouBaseInfo> findByTaskid(String taskid);
}
