package com.microservice.dao.repository.crawler.telecom.guangdong;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.telecom.guangdong.TelecomGuangDongUserInfo;

@Repository
public interface TelecomGuangDongUserInfoRepository extends JpaRepository<TelecomGuangDongUserInfo, Long>{

}
