/**
 * 
 */
package com.microservice.dao.repository.crawler.insurance.zhenjiang;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.insurance.zhenjiang.InsuranceZhenJiangParams;

/**
 * @author sln
 * @date 2018年8月3日下午7:17:50
 * @Description: 
 */
@Repository
public interface InsuranceZhenJiangParamsRepository extends JpaRepository<InsuranceZhenJiangParams, Long> {
	InsuranceZhenJiangParams findTopByTaskidOrderByCreatetimeDesc(String taskid);
}
