package com.microservice.dao.repository.crawler.housing.guilin;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.housing.guilin.HousingGuiLinPay;

@Repository
public interface HousingGuiLinPayRepository extends JpaRepository<HousingGuiLinPay, Long>{

}
