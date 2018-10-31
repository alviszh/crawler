package com.microservice.dao.repository.crawler.housing.tianjin;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.housing.tianjin.HousingTianJinYZM;

/**
 * @description:
 * @author: sln 
 * @date: 2017年9月29日 上午10:28:43 
 */
@Repository
public interface HousingTianJinYZMRepository extends JpaRepository<HousingTianJinYZM, Long> {

	HousingTianJinYZM findByTaskid(String taskid);
	
	
	@Transactional
	@Modifying
	@Query(value = "update HousingTianJinYZM set coordinate=?1 where taskid=?2")
	void updateCoordinate(String coordinate,String taskid);
}
