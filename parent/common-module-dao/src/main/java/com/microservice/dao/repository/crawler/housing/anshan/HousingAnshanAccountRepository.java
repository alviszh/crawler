package com.microservice.dao.repository.crawler.housing.anshan;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.housing.anshan.HousingAnShanAccount;

@Repository
public interface HousingAnshanAccountRepository extends JpaRepository<HousingAnShanAccount,Long>{

}
