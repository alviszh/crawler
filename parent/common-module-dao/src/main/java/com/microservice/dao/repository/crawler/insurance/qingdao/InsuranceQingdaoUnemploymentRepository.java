package com.microservice.dao.repository.crawler.insurance.qingdao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.insurance.qingdao.InsuranceQingdaoUnemployment;

public interface InsuranceQingdaoUnemploymentRepository extends JpaRepository<InsuranceQingdaoUnemployment, Long> {
	List<InsuranceQingdaoUnemployment> findByTaskid(String taskid);
}
