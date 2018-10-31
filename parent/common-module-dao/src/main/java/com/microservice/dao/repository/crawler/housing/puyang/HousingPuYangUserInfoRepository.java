package com.microservice.dao.repository.crawler.housing.puyang;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.housing.puyang.HousingPuYangUserInfo;



@Repository
public interface HousingPuYangUserInfoRepository extends JpaRepository<HousingPuYangUserInfo, Long> {

}
