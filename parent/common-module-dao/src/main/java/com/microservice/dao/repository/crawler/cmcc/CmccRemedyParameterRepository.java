package com.microservice.dao.repository.crawler.cmcc;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.microservice.dao.entity.crawler.cmcc.CmccRemedyParameter;

public interface CmccRemedyParameterRepository extends JpaRepository<CmccRemedyParameter, Long>{

	List<CmccRemedyParameter> findByTaskId(String taskid);

}
