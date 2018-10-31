package com.microservice.dao.repository.crawler.insurance.tieling;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.insurance.tieling.InsuranceTieLingUserInfo;

/**
 * @description:
 * @author: sln 
 * @date: 2017年12月12日 下午5:34:56 
 */
@Repository
public interface InsuranceTieLingUserInfoRepository extends JpaRepository<InsuranceTieLingUserInfo, Long> {

}
