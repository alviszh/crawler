package com.microservice.dao.repository.crawler.housing.zhumadian;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.housing.zhumadian.HousingFundZhuMaDianAccount;

@Repository
public interface HousingFundZhuMaDianRepositoryAccount extends JpaRepository<HousingFundZhuMaDianAccount,Long>{

}
