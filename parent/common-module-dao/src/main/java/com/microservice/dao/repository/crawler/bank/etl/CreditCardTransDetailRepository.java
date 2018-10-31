package com.microservice.dao.repository.crawler.bank.etl;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.microservice.dao.entity.crawler.bank.etl.CreditCardTransDetail;


public interface CreditCardTransDetailRepository extends JpaRepository<CreditCardTransDetail, Long>{

	List<CreditCardTransDetail> findByTaskId(String taskid);

	/**
	 * @Description
	 * @author sln
	 * @date 2018年9月13日 下午1:59:30
	 */
	@Query(value="select count(*) from CreditCardTransDetail where taskId =?1")
	int countEltTreatResultByTaskId(String taskid);
}
