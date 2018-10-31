package com.microservice.dao.repository.crawler.telecom.shanxi3;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.telecom.shanxi3.TelecomShanxi3CallRecord;

/**
 * @Description
 * @author sln
 * @date 2017年8月23日 下午3:30:57
 */
@Repository
public interface TelecomShanxi3CallRecordRepository extends JpaRepository<TelecomShanxi3CallRecord, Long> {
	int countByTaskid(String taskid); 
}

