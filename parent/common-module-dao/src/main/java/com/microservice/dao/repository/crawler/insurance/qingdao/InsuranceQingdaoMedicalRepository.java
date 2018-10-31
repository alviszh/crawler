package com.microservice.dao.repository.crawler.insurance.qingdao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.insurance.qingdao.InsuranceQingdaoMedical;

public interface InsuranceQingdaoMedicalRepository extends JpaRepository<InsuranceQingdaoMedical, Long> {
	List<InsuranceQingdaoMedical> findByTaskid(String taskid);
}
