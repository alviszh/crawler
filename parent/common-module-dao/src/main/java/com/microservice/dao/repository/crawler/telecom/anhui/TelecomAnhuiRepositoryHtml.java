package com.microservice.dao.repository.crawler.telecom.anhui;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.telecom.anhui.TelecomAnhuiHtml;

@Repository
public interface TelecomAnhuiRepositoryHtml extends JpaRepository<TelecomAnhuiHtml,Long>{

}
