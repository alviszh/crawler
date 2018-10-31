package com.microservice.dao.repository.crawler.insurance.linyi;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.insurance.linyi.InsuranceLinYiUserInfo;

/**
 * @description:
 * @author: sln 
 * @date: 2017年12月7日 下午6:40:11 
 */
@Repository
public interface InsuranceLinYiUserInfoRepository extends JpaRepository<InsuranceLinYiUserInfo, Long> {

}
