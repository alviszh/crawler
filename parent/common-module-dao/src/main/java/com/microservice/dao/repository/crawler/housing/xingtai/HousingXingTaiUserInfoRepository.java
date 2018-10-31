package com.microservice.dao.repository.crawler.housing.xingtai;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.housing.xingtai.HousingXingTaiUserInfo;

@Repository
public interface HousingXingTaiUserInfoRepository extends JpaRepository<HousingXingTaiUserInfo, Long> {

}
