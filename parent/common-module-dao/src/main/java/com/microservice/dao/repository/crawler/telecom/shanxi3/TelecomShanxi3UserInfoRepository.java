package com.microservice.dao.repository.crawler.telecom.shanxi3;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.telecom.shanxi3.TelecomShanxi3UserInfo;

/**
 * @Description
 * @author sln
 * @date 2017年8月23日 下午2:56:35
 */
@Repository
public interface TelecomShanxi3UserInfoRepository extends JpaRepository<TelecomShanxi3UserInfo, Long> {

}

