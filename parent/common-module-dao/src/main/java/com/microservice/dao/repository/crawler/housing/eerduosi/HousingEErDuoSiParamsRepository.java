/**
 * 
 */
package com.microservice.dao.repository.crawler.housing.eerduosi;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.housing.eerduosi.HousingEErDuoSiParams;

/**
 * @author sln
 * @date 2018年8月8日下午7:01:48
 * @Description: 
 */
@Repository
public interface HousingEErDuoSiParamsRepository extends JpaRepository<HousingEErDuoSiParams, Long> {
	HousingEErDuoSiParams findTopByTaskidOrderByCreatetimeDesc(String taskid);
}
