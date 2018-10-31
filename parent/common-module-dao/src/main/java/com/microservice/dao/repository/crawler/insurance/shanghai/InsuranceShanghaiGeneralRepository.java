package com.microservice.dao.repository.crawler.insurance.shanghai;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.insurance.shanghai.InsuranceShanghaiGeneral;

public interface InsuranceShanghaiGeneralRepository extends JpaRepository<InsuranceShanghaiGeneral, Long>{
	List<InsuranceShanghaiGeneral> findByTaskid(String taskid);
}
