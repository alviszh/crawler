package com.microservice.dao.repository.crawler.telecom.tianjin;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.telecom.tianjin.TelecomTianjinHtml;

@Repository
public interface TelecomTianjinHtmlRepository extends JpaRepository<TelecomTianjinHtml, Long> {

}

