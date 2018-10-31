package com.microservice.dao.repository.crawler.housing.lishui;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.housing.lishui.HousingLiShuiDetailAccount;



@Repository
public interface HousingLiShuiDetailAccountRepository extends JpaRepository<HousingLiShuiDetailAccount, Long> {

}
