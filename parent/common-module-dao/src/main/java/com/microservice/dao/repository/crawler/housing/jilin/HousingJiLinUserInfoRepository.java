package com.microservice.dao.repository.crawler.housing.jilin;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.housing.jilin.HousingJiLinUserInfo;


/**
 * @description:
 * @author: sln 
 * @date: 
 */
@Repository
public interface HousingJiLinUserInfoRepository extends JpaRepository<HousingJiLinUserInfo, Long> {

}
