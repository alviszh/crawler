package com.microservice.dao.repository.crawler.housing.shiyan;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.housing.shiyan.HousingFundShiYanUserInfo;

@Repository
public interface HousingFundShiYanRepositoryUserInfo extends JpaRepository<HousingFundShiYanUserInfo,Long>{

}
