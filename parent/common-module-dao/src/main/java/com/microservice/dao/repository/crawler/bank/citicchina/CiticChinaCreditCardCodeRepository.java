package com.microservice.dao.repository.crawler.bank.citicchina;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.bank.citicchina.CiticChinaCreditCardCode;

@Repository
public interface CiticChinaCreditCardCodeRepository extends JpaRepository<CiticChinaCreditCardCode,Long>{

	CiticChinaCreditCardCode findByTaskid(String taskid);
	
	@Transactional
	@Modifying
	@Query(value = "update CiticChinaCreditCardCode set code=?1 where taskid=?2")
	void updateCode(String code, String taskid);
}
