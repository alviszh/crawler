package com.microservice.dao.repository.crawler.housing.haerbin;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.housing.haerbin.HousingFundHaErBinUserInfo;
@Repository
public interface HousingHaErBinUserInfoRepository extends JpaRepository<HousingFundHaErBinUserInfo,Long>{

}
