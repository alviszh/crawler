package com.microservice.dao.repository.crawler.housing.tongliao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.housing.tongliao.HousingFundTongLiaoUserInfo;

@Repository
public interface HousingFundTongLiaoRepositoryUserInfo extends JpaRepository<HousingFundTongLiaoUserInfo,Long>{

}
