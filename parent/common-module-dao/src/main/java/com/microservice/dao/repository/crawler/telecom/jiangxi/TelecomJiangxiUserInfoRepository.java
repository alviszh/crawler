package com.microservice.dao.repository.crawler.telecom.jiangxi;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.telecom.jiangxi.TelecomJiangxiUserInfo;

@Repository
public interface TelecomJiangxiUserInfoRepository extends JpaRepository<TelecomJiangxiUserInfo, Long> {

}

