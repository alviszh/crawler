/**
 * 
 */
package com.microservice.dao.repository.crawler.housing.jilin;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.housing.jilin.HousingJiLinParams;

@Repository
public interface HousingJiLinParamsRepository extends JpaRepository<HousingJiLinParams, Long> {
	HousingJiLinParams findTopByTaskidOrderByCreatetimeDesc(String taskid);
}
