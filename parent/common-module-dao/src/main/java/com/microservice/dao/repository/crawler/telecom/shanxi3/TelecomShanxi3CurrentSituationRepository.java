package com.microservice.dao.repository.crawler.telecom.shanxi3;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.telecom.shanxi3.TelecomShanxi3CurrentSituation;

@Repository
public interface TelecomShanxi3CurrentSituationRepository extends JpaRepository<TelecomShanxi3CurrentSituation, Long> {

}
