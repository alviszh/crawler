package com.microservice.dao.repository.crawler.bank.hxbchina;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.bank.hxbchina.HxbChinaDebitCardUserInfo;


/**
 * @description:
 * @author: sln 
 * @date: 2017年11月8日 下午5:54:19 
 */
@Repository
public interface HxbChinaDebitCardUserInfoRepository extends JpaRepository<HxbChinaDebitCardUserInfo, Long> {

}
