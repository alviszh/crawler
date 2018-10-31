package com.microservice.dao.repository.crawler.bank.etl;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.microservice.dao.entity.crawler.bank.etl.DebiCardTransDetail;

public interface DebiCardTransDetailRepository extends JpaRepository<DebiCardTransDetail, Long>{

	List<DebiCardTransDetail> findByTaskId(String taskid);

	/**
	 * @Description
	 * @author sln
	 * @date 2018年9月13日 下午1:58:45
	 */
	@Query(value="select count(*) from DebiCardTransDetail where taskId =?1")
	int countEltTreatResultByTaskId(String taskid);

}
