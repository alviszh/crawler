package com.microservice.dao.repository.crawler.telecom.shanxi1;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.telecom.shanxi1.TelecomShanxi1PayInfo;

@Repository
public interface TelecomShanxi1PayInfoRepository extends JpaRepository<TelecomShanxi1PayInfo, Long>{
	
}
 