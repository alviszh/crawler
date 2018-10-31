package com.microservice.dao.repository.crawler.insurance.ningbo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.insurance.ningbo.InsuranceNingboEndowment;
@Repository
public interface InsuranceNingboEndowmentRepository extends JpaRepository<InsuranceNingboEndowment, Long>{
	List<InsuranceNingboEndowment> findByTaskid(String taskid);
}
