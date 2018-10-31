package com.microservice.dao.repository.crawler.telecom.common;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.telecom.common.TelecomStarlevel;

@Repository
public interface TelecomCommonStarlevelRepository extends JpaRepository<TelecomStarlevel, Long>{
	
}
 