package com.microservice.dao.repository.crawler.telecom.common;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.telecom.common.TelecomCommonPointsAndCharges;

@Repository
public interface TelecomCommonPointsAndChargesRepository extends JpaRepository<TelecomCommonPointsAndCharges, Long>{

}
 