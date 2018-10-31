package com.microservice.dao.repository.crawler.insurance.guangzhou;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.insurance.guangzhou.GuangzhouMedicalInsurance;

public interface GuangZhouMedicalInsuranceRepository extends JpaRepository<GuangzhouMedicalInsurance, Long>{

	List<GuangzhouMedicalInsurance> findByTaskid(String taskid);
	
}
