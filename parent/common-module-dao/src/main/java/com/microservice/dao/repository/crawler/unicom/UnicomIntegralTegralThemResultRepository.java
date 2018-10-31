package com.microservice.dao.repository.crawler.unicom;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.unicom.UnicomIntegralTotalResult;

@Repository 
public interface UnicomIntegralTegralThemResultRepository extends JpaRepository<UnicomIntegralTotalResult, Long>{
	
	List<UnicomIntegralTotalResult> findByTaskid(String taskId);
}
 