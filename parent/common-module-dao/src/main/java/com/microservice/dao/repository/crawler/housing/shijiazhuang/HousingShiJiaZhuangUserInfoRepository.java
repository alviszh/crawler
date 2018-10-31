package com.microservice.dao.repository.crawler.housing.shijiazhuang;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.housing.shijiazhuang.HousingShiJiaZhuangUserInfo;

/**
 * @description:
 * @author: sln 
 * @date: 2017年10月19日 下午2:47:23 
 */
@Repository
public interface HousingShiJiaZhuangUserInfoRepository extends JpaRepository<HousingShiJiaZhuangUserInfo, Long> {

}
