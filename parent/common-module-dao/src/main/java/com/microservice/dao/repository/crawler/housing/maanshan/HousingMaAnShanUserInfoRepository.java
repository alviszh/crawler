package com.microservice.dao.repository.crawler.housing.maanshan;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.housing.maanshan.HousingMaAnShanUserInfo;


@Repository
public interface HousingMaAnShanUserInfoRepository extends JpaRepository<HousingMaAnShanUserInfo, Long> {

}
