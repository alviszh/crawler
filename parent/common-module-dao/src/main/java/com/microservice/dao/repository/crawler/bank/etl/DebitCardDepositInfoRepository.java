package com.microservice.dao.repository.crawler.bank.etl;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.bank.etl.DebitCardDepositInfo;


public interface DebitCardDepositInfoRepository extends JpaRepository<DebitCardDepositInfo, Long>{

	List<DebitCardDepositInfo> findByTaskId(String taskid);

}
