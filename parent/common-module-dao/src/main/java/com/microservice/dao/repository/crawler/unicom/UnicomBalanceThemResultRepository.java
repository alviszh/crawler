package com.microservice.dao.repository.crawler.unicom;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.unicom.UnicomBalance;

@Repository 
public interface UnicomBalanceThemResultRepository extends JpaRepository<UnicomBalance, Long>{

	List<UnicomBalance> findByTaskid(String taskId);
}
 