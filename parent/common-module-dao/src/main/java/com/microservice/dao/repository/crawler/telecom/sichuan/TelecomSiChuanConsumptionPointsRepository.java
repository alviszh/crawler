package com.microservice.dao.repository.crawler.telecom.sichuan;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.telecom.sichuan.TelecomSiChuanConsumptionPoints;

@Repository
public interface TelecomSiChuanConsumptionPointsRepository extends JpaRepository<TelecomSiChuanConsumptionPoints, Long>{

}
