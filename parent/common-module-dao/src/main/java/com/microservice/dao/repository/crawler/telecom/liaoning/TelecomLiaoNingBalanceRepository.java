package com.microservice.dao.repository.crawler.telecom.liaoning;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.telecom.liaoning.TelecomLiaoNingBalance;
@Repository
public interface TelecomLiaoNingBalanceRepository extends JpaRepository<TelecomLiaoNingBalance, Long>{

}
