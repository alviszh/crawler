package com.microservice.dao.repository.crawler.housing.tonghua;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.housing.tonghua.HousingFundTongHuaAccount;
@Repository
public interface HousingFundRepositoryTongHuaAccount extends JpaRepository<HousingFundTongHuaAccount,Long>{

}
