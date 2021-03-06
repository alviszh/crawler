package com.microservice.dao.repository.crawler.telecom.gansu;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.telecom.gansu.TelecomGansuCall;

public interface TelecomGansuCallRepository extends JpaRepository<TelecomGansuCall,Long>{
	//勿删   2018年4月8日为统计指定taskid下的通话记录总数而添加的方法
	int countByTaskid(String taskid); 
}
