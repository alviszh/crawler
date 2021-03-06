package com.microservice.dao.repository.crawler.telecom.hubei;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.telecom.hubei.TelecomHubeiHtml;

@Repository
public interface TelecomHubeiHtmlRepository extends JpaRepository<TelecomHubeiHtml, Long>{

}
