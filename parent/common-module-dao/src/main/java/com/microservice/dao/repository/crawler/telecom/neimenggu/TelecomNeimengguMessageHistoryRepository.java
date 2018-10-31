package com.microservice.dao.repository.crawler.telecom.neimenggu;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.telecom.neimenggu.TelecomNeimengguMessageHistory;

@Repository
public interface TelecomNeimengguMessageHistoryRepository extends JpaRepository<TelecomNeimengguMessageHistory, Long>{

}
 