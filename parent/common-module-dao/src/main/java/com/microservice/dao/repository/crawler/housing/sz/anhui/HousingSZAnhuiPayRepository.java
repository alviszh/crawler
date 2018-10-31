package com.microservice.dao.repository.crawler.housing.sz.anhui;

import com.microservice.dao.entity.crawler.housing.sz.anhui.HousingSZAnhuiPay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HousingSZAnhuiPayRepository extends JpaRepository<HousingSZAnhuiPay,Long>{
}
