package com.microservice.dao.repository.crawler.housing.dezhou;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.housing.dezhou.HousingDeZhouDepositInformation;

@Repository
public interface HousingDezhouDepositRepository extends JpaRepository<HousingDeZhouDepositInformation,Long>{

}
