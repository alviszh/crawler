package com.microservice.dao.repository.crawler.insurance.etl;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.insurance.etl.ETLInsuranceUserinfo;


public interface ETLInsuranceUserinfoRepository extends JpaRepository<ETLInsuranceUserinfo, Long>{

	List<ETLInsuranceUserinfo> findByTaskId(String taskid);

}
