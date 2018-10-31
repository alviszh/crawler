package com.microservice.dao.repository.crawler.insurance.guangzhou;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.insurance.guangzhou.InsuranceGuangZhouGeneral;

public interface InsuranceGuangZhouGeneralRepository extends JpaRepository<InsuranceGuangZhouGeneral, Long>{

	List<InsuranceGuangZhouGeneral> findByTaskid(String taskid);
}
