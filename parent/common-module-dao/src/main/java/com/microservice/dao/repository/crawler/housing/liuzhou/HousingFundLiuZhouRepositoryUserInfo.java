package com.microservice.dao.repository.crawler.housing.liuzhou;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.housing.liuzhou.HousingFundLiuZhouUserinfo;

@Repository
public interface HousingFundLiuZhouRepositoryUserInfo extends JpaRepository<HousingFundLiuZhouUserinfo,Long>{

}
