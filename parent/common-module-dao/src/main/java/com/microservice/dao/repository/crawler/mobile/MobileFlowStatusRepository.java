package com.microservice.dao.repository.crawler.mobile;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.mobile.MobileFlowStatus;

public interface MobileFlowStatusRepository extends JpaRepository<MobileFlowStatus, Long>{

}
