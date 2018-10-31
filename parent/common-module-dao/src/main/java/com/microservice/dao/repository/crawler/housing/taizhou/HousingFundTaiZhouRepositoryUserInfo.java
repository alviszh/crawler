package com.microservice.dao.repository.crawler.housing.taizhou;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.housing.taizhou.HousingFundTaiZhouUserInfo;

@Repository
public interface HousingFundTaiZhouRepositoryUserInfo extends JpaRepository<HousingFundTaiZhouUserInfo,Long>{

}
