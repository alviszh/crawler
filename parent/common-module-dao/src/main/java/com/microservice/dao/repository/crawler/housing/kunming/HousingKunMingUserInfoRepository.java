package com.microservice.dao.repository.crawler.housing.kunming;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.housing.kunming.HousingKunMingUserInfo;


@Repository
public interface HousingKunMingUserInfoRepository extends JpaRepository<HousingKunMingUserInfo, Long> {

}
