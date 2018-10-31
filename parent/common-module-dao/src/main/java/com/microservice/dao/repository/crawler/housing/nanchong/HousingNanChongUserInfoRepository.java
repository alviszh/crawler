package com.microservice.dao.repository.crawler.housing.nanchong;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.housing.nanchong.HousingNanChongUserInfo;




@Repository
public interface HousingNanChongUserInfoRepository extends JpaRepository<HousingNanChongUserInfo, Long> {

}
