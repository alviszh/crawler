package com.microservice.dao.repository.crawler.housing.hengshui;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.housing.hengshui.HousingHengShuiDetailAccount;
@Repository
public interface HousingHengShuiDetailAccountRepository extends JpaRepository<HousingHengShuiDetailAccount, Long> {

}
