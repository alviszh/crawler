package com.microservice.dao.repository.crawler.telecom.heilongjiang;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.telecom.heilongjiang.TelecomCustomerThemResult;



@Repository
public interface TelecomCustomThemResultRepository extends JpaRepository<TelecomCustomerThemResult, Long>{
	List<TelecomCustomerThemResult> findByTaskid(String taskId);
}
 