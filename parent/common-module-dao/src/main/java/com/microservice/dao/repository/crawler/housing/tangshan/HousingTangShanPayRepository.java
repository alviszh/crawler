package com.microservice.dao.repository.crawler.housing.tangshan;


import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.housing.tangshan.HousingTangShanPay;

public interface HousingTangShanPayRepository extends JpaRepository<HousingTangShanPay, Long>{

	HousingTangShanPay findByTaskid(String taskid);
}
