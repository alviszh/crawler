package com.microservice.dao.repository.crawler.e_commerce.etl.suning;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.e_commerce.etl.suning.AddressInfoSuning;


public interface AddressInfoSuningRepository extends JpaRepository<AddressInfoSuning, Long>{

	List<AddressInfoSuning> findByTaskId(String taskid);

}
