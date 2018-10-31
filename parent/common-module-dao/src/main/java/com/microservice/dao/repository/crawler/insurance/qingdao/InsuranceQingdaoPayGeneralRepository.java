package com.microservice.dao.repository.crawler.insurance.qingdao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.insurance.qingdao.InsuranceQingdaoPayGeneral;

public interface InsuranceQingdaoPayGeneralRepository extends JpaRepository<InsuranceQingdaoPayGeneral, Long> {
	List<InsuranceQingdaoPayGeneral> findByTaskid(String taskid);
}
