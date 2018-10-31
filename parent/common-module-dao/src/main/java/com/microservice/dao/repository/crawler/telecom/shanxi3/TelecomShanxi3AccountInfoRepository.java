package com.microservice.dao.repository.crawler.telecom.shanxi3;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.telecom.shanxi3.TelecomShanxi3AccountInfo;

/**
 * @Description
 * @author sln
 * @date 2017年8月24日 上午10:39:13
 */
@Repository
public interface TelecomShanxi3AccountInfoRepository extends JpaRepository<TelecomShanxi3AccountInfo, Long> {

}

