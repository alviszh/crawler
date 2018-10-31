package com.microservice.dao.repository.crawler.housing.bengbu;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.housing.bengbu.HousingFundBengBuAccount;

@Repository
public interface HousingFundBengBuRepositoryAccount extends JpaRepository<HousingFundBengBuAccount, Long>{

}
