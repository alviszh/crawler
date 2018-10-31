package com.microservice.dao.repository.crawler.unicom;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.unicom.UnicomDetailList;

@Repository 
public interface UnicomDetailListRepository extends JpaRepository<UnicomDetailList, Long>{
	
	
	List<UnicomDetailList> findByTaskid(String taskId);

}
 