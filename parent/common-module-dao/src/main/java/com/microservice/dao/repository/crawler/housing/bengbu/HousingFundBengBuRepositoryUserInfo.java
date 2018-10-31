package com.microservice.dao.repository.crawler.housing.bengbu;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.housing.bengbu.HousingFundBengBuHtml;
import com.microservice.dao.entity.crawler.housing.bengbu.HousingFundBengBuUserInfo;

@Repository
public interface HousingFundBengBuRepositoryUserInfo extends JpaRepository<HousingFundBengBuUserInfo, Long>{

}
