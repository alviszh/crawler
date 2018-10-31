package com.microservice.dao.repository.crawler.housing.zhenjiang;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.housing.zhenjiang.HousingZhenJiangDetailAccount;
@Repository
public interface HousingZhenJiangDetailAccountRepository extends JpaRepository<HousingZhenJiangDetailAccount, Long> {

}
