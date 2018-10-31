package com.microservice.dao.repository.crawler.housing.qinzhou;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.housing.qinzhou.HousingFundQinZhouUserInfo;

@Repository
public interface HousingFundQinZhouRepositoryUserInfo extends JpaRepository<HousingFundQinZhouUserInfo,Long>{

}
