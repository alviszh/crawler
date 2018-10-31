package com.microservice.dao.repository.crawler.insurance.etl;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.microservice.dao.entity.crawler.insurance.etl.ETLInsuranceDetail;


public interface ETLInsuranceDetailRepository extends JpaRepository<ETLInsuranceDetail, Long>{

	List<ETLInsuranceDetail> findByTaskId(String taskid);

	/**
	 * @Description
	 * @author sln
	 * @date 2018年9月13日 上午11:36:16
	 */
	@Query(value="select count(*) from ETLInsuranceDetail where taskId =?1")
	int countEltTreatResultByTaskId(String taskid);

}
