package com.microservice.dao.repository.crawler.telecom.sichuan;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.telecom.sichuan.TelecomSiChuanPhoneBill;

@Repository
public interface TelecomSiChuanPhoneBillRepository extends JpaRepository<TelecomSiChuanPhoneBill, Long>{

}
