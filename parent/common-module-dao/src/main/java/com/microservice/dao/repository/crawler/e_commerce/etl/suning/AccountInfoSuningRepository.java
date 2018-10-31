package com.microservice.dao.repository.crawler.e_commerce.etl.suning;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.e_commerce.etl.suning.AccountInfoSuning;

public interface AccountInfoSuningRepository extends JpaRepository<AccountInfoSuning, Long>{

	List<AccountInfoSuning> findByTaskId(String taskid);


}
