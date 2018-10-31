package com.microservice.dao.repository.crawler.housing.zhuzhou;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.housing.zhuzhou.HousingFundZhuZhouAccount;
import com.microservice.dao.entity.crawler.housing.zhuzhou.HousingFundZhuZhouHtml;

@Repository
public interface HousingFundZhuZhouRepositoryAccount extends JpaRepository<HousingFundZhuZhouAccount, Long>{

}
