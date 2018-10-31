package com.microservice.dao.repository.crawler.housing.eerduosi;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.housing.eerduosi.HousingEErDuoSiUserInfo;

/**
 * @description:
 * @author: sln 
 * @date: 2018年1月17日 下午3:26:01 
 */
@Repository
public interface HousingEErDuoSiUserInfoRepository extends JpaRepository<HousingEErDuoSiUserInfo, Long> {

}
