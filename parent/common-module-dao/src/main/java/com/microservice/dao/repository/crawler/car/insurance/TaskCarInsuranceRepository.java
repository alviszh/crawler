package com.microservice.dao.repository.crawler.car.insurance;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.car.insurance.TaskCarInsurance;

public interface TaskCarInsuranceRepository extends JpaRepository<TaskCarInsurance, Long> {

	TaskCarInsurance findByTaskid(String taskid);

}
