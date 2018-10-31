package com.microservice.dao.repository.crawler.telecom.guangxi;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.telecom.guangxi.TelecomGuangxiHtml;
@Repository
public interface TelecomGuangxiRepositoryHtml  extends JpaRepository<TelecomGuangxiHtml,Long>{

}
