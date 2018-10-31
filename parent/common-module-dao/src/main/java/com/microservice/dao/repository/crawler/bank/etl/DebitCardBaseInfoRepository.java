package com.microservice.dao.repository.crawler.bank.etl;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.bank.etl.DebitCardBaseInfo;


public interface DebitCardBaseInfoRepository extends JpaRepository<DebitCardBaseInfo, Long>{

	List<DebitCardBaseInfo> findByTaskId(String taskid);

}
