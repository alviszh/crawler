package com.microservice.dao.repository.crawler.housing.chifeng;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.housing.chifeng.HousingFundChiFengUserInfo;

@Repository
public interface HousingFundChiFengRepositoryUserInfo extends JpaRepository<HousingFundChiFengUserInfo,Long>{

}
