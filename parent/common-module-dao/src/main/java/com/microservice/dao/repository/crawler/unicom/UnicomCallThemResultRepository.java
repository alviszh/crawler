package com.microservice.dao.repository.crawler.unicom;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.unicom.UnicomCallResult;
import java.lang.String;

@Repository 
public interface UnicomCallThemResultRepository extends JpaRepository<UnicomCallResult, Long>{
	
	
	List<UnicomCallResult> findByUsernumber(String usernumber);

	List<UnicomCallResult> findByUsernumberAndCalldateBetween(String usernumber,String stardate,String enddate);
	
	List<UnicomCallResult> findByTaskid(String taskId);
	
	//勿删   2018年4月8日为统计指定taskid下的通话记录总数而添加的方法
	int countByTaskid(String taskid); 
}
 