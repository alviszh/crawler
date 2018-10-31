package com.microservice.dao.repository.crawler.housing.guangzhou;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.housing.guangzhou.HousingGuangzhouUserInfo;

/**
 * @description:
 * @author: sln 
 * @date: 2017年9月29日 上午10:29:27 
 */
@Repository
public interface HousingGuangzhouUserInfoRepository extends JpaRepository<HousingGuangzhouUserInfo, Long> {

}
