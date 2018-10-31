package com.microservice.dao.repository.crawler.housing.huaibei;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.housing.huaibei.HousingHuaiBeiBase;

@Repository
public interface HousingHuaiBeiBaseRepository extends JpaRepository<HousingHuaiBeiBase,Long>{

}
