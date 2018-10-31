package com.microservice.dao.repository.crawler.telecom.guizhou;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.telecom.guizhou.TelecomGuizhouHtml;

@Repository
public interface TelecomGuizhouHtmlRepository extends JpaRepository<TelecomGuizhouHtml, Long>{

}
