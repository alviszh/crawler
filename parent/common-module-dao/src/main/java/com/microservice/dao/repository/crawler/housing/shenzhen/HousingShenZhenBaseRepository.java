package com.microservice.dao.repository.crawler.housing.shenzhen;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.housing.shenzhen.HousingShenZhenBase;

/**
 * @description:
 * @author: qzb 
 * @date: 2017年9月29日 上午10:28:43 
 */
@Repository
public interface HousingShenZhenBaseRepository extends JpaRepository<HousingShenZhenBase, Long> {

	HousingShenZhenBase findByTaskid(String taskid);
	
}
