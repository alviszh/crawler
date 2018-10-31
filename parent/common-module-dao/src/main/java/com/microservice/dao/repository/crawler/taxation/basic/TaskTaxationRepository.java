package com.microservice.dao.repository.crawler.taxation.basic;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.taxation.basic.TaskTaxation;
@Repository
public interface TaskTaxationRepository extends JpaRepository<TaskTaxation, Long>, JpaSpecificationExecutor<TaskTaxation>{

		TaskTaxation findByTaskid(String taskId);
		
		Page<TaskTaxation> findAll(Pageable pageable);
}
