package com.microservice.dao.repository.crawler.insurance.qingdao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.insurance.qingdao.InsuranceQingdaoPension;

public interface InsuranceQingdaoPensionRepository extends JpaRepository<InsuranceQingdaoPension, Long> {
	List<InsuranceQingdaoPension> findByTaskid(String taskid);
}
