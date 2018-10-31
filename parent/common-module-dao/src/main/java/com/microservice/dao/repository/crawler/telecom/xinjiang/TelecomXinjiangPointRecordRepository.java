package com.microservice.dao.repository.crawler.telecom.xinjiang;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.telecom.xinjiang.TelecomXinjiangPointRecord;

@Repository
public interface TelecomXinjiangPointRecordRepository extends JpaRepository<TelecomXinjiangPointRecord, Long>{

}
 