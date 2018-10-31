package com.microservice.dao.repository.crawler.housing.shenyang;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.housing.shenyang.HousingShenYangUserInfo;

@Repository
public interface HousingShenYangUserInfoRepository extends JpaRepository<HousingShenYangUserInfo,Long>{

}
