package com.microservice.dao.repository.crawler.housing.huangshi;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.housing.huangshi.HousingFundHuangShiAccount;

@Repository
public interface HousingFundHuangShiRepositoryAccount extends JpaRepository<HousingFundHuangShiAccount,Long>{

}
