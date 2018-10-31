package com.microservice.dao.repository.crawler.telecom.anhui;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.telecom.anhui.TelecomAnhuiBusiness;

@Repository
public interface TelecomAnhuiRepositoryBusiness extends JpaRepository<TelecomAnhuiBusiness,Long>{

}
