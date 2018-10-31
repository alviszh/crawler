package com.microservice.dao.repository.crawler.housing.hulunbeier;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.housing.hulunbeier.HousingHuLunBeiErUserInfo;

@Repository
public interface HousingHuLunBeiErUserInfoRepository extends JpaRepository<HousingHuLunBeiErUserInfo, Long>{

}
