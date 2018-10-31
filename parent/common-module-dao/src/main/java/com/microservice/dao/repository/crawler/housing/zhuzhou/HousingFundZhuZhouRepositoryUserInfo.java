package com.microservice.dao.repository.crawler.housing.zhuzhou;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.housing.zhuzhou.HousingFundZhuZhouUserInfo;

@Repository
public interface HousingFundZhuZhouRepositoryUserInfo extends JpaRepository<HousingFundZhuZhouUserInfo, Long>{

}
