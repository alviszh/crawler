package com.microservice.dao.repository.crawler.insurance.leshan;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.insurance.leshan.InsuranceLeShanYiLiao;

@Repository
public interface InsuranceLeShanYiLiaoRepository extends JpaRepository<InsuranceLeShanYiLiao, Long>{

}
