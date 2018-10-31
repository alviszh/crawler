package com.microservice.dao.repository.crawler.housing.zhanjiang;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.housing.zhanjiang.HousingFundZhanJiangHtml;
import com.microservice.dao.entity.crawler.housing.zhanjiang.HousingFundZhanJiangUserInfo;

@Repository
public interface HousingFundZhanJiangRepositoryUserInfo extends JpaRepository<HousingFundZhanJiangUserInfo, Long>{

}
