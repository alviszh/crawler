package com.microservice.dao.repository.crawler.housing.huhehaote;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.housing.huhehaote.HousingHuHeHaoTeDetailAccount;
@Repository
public interface HousingHuHeHaoTeDetailAccountRepository extends JpaRepository<HousingHuHeHaoTeDetailAccount, Long> {

}
