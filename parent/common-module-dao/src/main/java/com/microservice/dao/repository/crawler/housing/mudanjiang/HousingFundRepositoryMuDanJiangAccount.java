package com.microservice.dao.repository.crawler.housing.mudanjiang;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.housing.mudanjiang.HousingFundMuDanJiangAccount;

@Repository
public interface HousingFundRepositoryMuDanJiangAccount extends JpaRepository<HousingFundMuDanJiangAccount,Long>{

}
