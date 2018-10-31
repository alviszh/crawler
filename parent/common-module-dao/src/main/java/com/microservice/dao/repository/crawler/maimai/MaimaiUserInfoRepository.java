package com.microservice.dao.repository.crawler.maimai;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.maimai.MaimaiUserInfo;

public interface MaimaiUserInfoRepository extends JpaRepository<MaimaiUserInfo, Long>{

}
