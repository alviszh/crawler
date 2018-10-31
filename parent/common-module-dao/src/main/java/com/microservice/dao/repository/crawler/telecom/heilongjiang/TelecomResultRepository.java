package com.microservice.dao.repository.crawler.telecom.heilongjiang;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.telecom.heilongjiang.TelecomCallThemResult;
import java.lang.String;

@Repository
public interface TelecomResultRepository extends JpaRepository<TelecomCallThemResult, Long>{
		
//	@Query(value = "select  *  from telecom_result where usernumber=?1", nativeQuery = true)
//	List<TelecomCallThemResult> findByUsernumber(String usernumber);
	
	List<TelecomCallThemResult> findByUsernumber(String usernumber);
	
//	@Query(value = "select  *  from telecom_result where usernumber=?1 and calldate between ?2 and ?3", nativeQuery = true)
//	List<TelecomCallThemResult> findByUsernumberAndDate(String usernumber,String stardate,String enddate);
	
	
	List<TelecomCallThemResult> findByTaskid(String taskId);
	
	//勿删   2018年4月8日为统计指定taskid下的通话记录总数而添加的方法
	int countByTaskid(String taskid); 
}
 