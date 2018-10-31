package com.microservice.dao.repository.crawler.housing.bozhou;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.housing.bozhou.HousingBoZhouUserInfo;

@Repository
public interface HousingBoZhouUserInfoRepository extends JpaRepository<HousingBoZhouUserInfo,Long>{

}
