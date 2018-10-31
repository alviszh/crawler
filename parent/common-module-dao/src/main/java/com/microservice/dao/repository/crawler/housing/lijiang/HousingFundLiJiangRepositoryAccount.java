package com.microservice.dao.repository.crawler.housing.lijiang;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.housing.lijiang.HousingFundLiJiangAccount;

@Repository
public interface HousingFundLiJiangRepositoryAccount extends JpaRepository<HousingFundLiJiangAccount,Long>{

}
