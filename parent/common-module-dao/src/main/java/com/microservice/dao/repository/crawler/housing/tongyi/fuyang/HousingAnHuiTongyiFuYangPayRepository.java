package com.microservice.dao.repository.crawler.housing.tongyi.fuyang;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.housing.tongyi.fuyang.HousingAnHuiTongyiFuYangPay;

@Repository
public interface HousingAnHuiTongyiFuYangPayRepository extends JpaRepository<HousingAnHuiTongyiFuYangPay, Long>{
	
}
