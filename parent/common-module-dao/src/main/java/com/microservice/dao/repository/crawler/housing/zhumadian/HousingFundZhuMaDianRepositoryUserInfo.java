package com.microservice.dao.repository.crawler.housing.zhumadian;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.housing.zhumadian.HousingFundZhuMaDianUserInfo;

@Repository
public interface HousingFundZhuMaDianRepositoryUserInfo extends JpaRepository<HousingFundZhuMaDianUserInfo,Long>{

}
