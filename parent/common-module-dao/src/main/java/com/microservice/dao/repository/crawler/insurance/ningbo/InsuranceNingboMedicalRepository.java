package com.microservice.dao.repository.crawler.insurance.ningbo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.insurance.ningbo.InsuranceNingboMedical;
@Repository
public interface InsuranceNingboMedicalRepository extends JpaRepository<InsuranceNingboMedical, Long>{

	List<InsuranceNingboMedical> findByTaskid(String taskid);
}
