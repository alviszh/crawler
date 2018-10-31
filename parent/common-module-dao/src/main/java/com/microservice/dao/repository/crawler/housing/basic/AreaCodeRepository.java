package com.microservice.dao.repository.crawler.housing.basic;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.housing.basic.AreaCode;

public interface AreaCodeRepository extends JpaRepository<AreaCode, Long>{

	List<AreaCode> findByIsHousingfundFinishedLessThan(int i);
	
	AreaCode findByTaskname(String taskname);

}
