package com.microservice.dao.repository.crawler.telecom.henan;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.telecom.henan.TelecomHenanMonthBill;

@Repository
public interface TelecomHenanMonthBillRepository extends JpaRepository<TelecomHenanMonthBill, Long>{

}
 