package com.microservice.dao.repository.crawler.housing.liuzhou;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.housing.liuzhou.HousingFundLiuZhouPay;

@Repository
public interface HousingFundLiuZhouRepositoryPay extends JpaRepository<HousingFundLiuZhouPay,Long> {

}
