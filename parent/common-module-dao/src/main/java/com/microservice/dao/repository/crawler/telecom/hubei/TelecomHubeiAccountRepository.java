package com.microservice.dao.repository.crawler.telecom.hubei;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.telecom.hubei.TelecomHubeiAccount;

@Repository
public interface TelecomHubeiAccountRepository extends JpaRepository<TelecomHubeiAccount, Long>{

}
