package com.microservice.dao.repository.crawler.housing.shenyang;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.housing.shenyang.HousingShenYangPay;

@Repository
public interface HousingShenYangPayRepository extends JpaRepository<HousingShenYangPay,Long>{

}
