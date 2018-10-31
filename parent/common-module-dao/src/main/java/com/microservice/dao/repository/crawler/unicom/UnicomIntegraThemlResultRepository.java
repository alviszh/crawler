package com.microservice.dao.repository.crawler.unicom;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.unicom.UnicomIntegraThemlResult;

@Repository 
public interface UnicomIntegraThemlResultRepository extends JpaRepository<UnicomIntegraThemlResult, Long>{
	
	List<UnicomIntegraThemlResult> findByTaskid(String taskId);
	
}
 