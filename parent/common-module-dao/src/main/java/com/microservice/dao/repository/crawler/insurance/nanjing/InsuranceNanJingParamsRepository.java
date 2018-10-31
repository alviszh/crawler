package com.microservice.dao.repository.crawler.insurance.nanjing;
/**
 * 
 */

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.insurance.nanjing.InsuranceNanJingParams;

/**
 * @author sln
 * @date 2018年8月3日下午7:17:50
 * @Description: 
 */
@Repository
public interface InsuranceNanJingParamsRepository extends JpaRepository<InsuranceNanJingParams, Long> {
	InsuranceNanJingParams findTopByTaskidOrderByCreatetimeDesc(String taskid);
}
