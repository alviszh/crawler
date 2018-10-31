package com.microservice.dao.repository.crawler.telecom.hubei;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.telecom.hubei.TelecomHubeiPaymonths;

@Repository
public interface TelecomHubeiPaymonthsRepository extends JpaRepository<TelecomHubeiPaymonths, Long>{

}
