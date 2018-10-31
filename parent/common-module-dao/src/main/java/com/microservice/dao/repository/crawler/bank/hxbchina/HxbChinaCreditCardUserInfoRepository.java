package com.microservice.dao.repository.crawler.bank.hxbchina;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.bank.hxbchina.HxbChinaCreditCardUserInfo;

/**
 * @description:
 * @author: sln 
 * @date: 2017年11月17日 上午11:48:41 
 */
@Repository
public interface HxbChinaCreditCardUserInfoRepository extends JpaRepository<HxbChinaCreditCardUserInfo, Long> {

}
