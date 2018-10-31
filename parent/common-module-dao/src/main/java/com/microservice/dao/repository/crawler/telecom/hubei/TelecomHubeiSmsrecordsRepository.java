package com.microservice.dao.repository.crawler.telecom.hubei;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.telecom.hubei.TelecomHubeiSmsrecords;

@Repository
public interface TelecomHubeiSmsrecordsRepository extends JpaRepository<TelecomHubeiSmsrecords, Long>{

}
