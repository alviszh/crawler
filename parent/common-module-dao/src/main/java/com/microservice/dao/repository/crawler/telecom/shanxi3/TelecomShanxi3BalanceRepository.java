package com.microservice.dao.repository.crawler.telecom.shanxi3;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.telecom.shanxi3.TelecomShanxi3Balance;
@Repository
public interface TelecomShanxi3BalanceRepository extends JpaRepository<TelecomShanxi3Balance, Long> {

}
