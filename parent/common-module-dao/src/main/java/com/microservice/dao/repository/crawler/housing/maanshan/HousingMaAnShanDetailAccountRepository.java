package com.microservice.dao.repository.crawler.housing.maanshan;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.housing.maanshan.HousingMaAnShanDetailAccount;
@Repository
public interface HousingMaAnShanDetailAccountRepository extends JpaRepository<HousingMaAnShanDetailAccount, Long> {

}
