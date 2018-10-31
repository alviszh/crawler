package com.microservice.dao.repository.crawler.insurance.tianjin;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.insurance.tianjin.InsuranceTianjinUnemployment;
@Repository
public interface InsuranceTianjinUnemploymentRepository extends JpaRepository<InsuranceTianjinUnemployment,Long>{

	List<InsuranceTianjinUnemployment> findByTaskid(String taskid);
}
