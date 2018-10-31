package com.microservice.dao.repository.crawler.housing.zaozhuang;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.housing.zaozhuang.HousingZaoZhuangBase;

/**
 * @description:
 * @author: sln 
 * @date: 2017年9月29日 上午10:28:43 
 */
@Repository
public interface HousingZaoZhuangBaseRepository extends JpaRepository<HousingZaoZhuangBase, Long> {

	HousingZaoZhuangBase findByTaskid(String taskid);
	
}
