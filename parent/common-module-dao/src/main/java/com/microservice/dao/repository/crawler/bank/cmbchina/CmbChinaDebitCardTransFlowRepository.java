package com.microservice.dao.repository.crawler.bank.cmbchina;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.bank.cmbchina.CmbChinaDebitCardTransFlow;

public interface CmbChinaDebitCardTransFlowRepository  extends JpaRepository<CmbChinaDebitCardTransFlow, Long>{
	
	//List<CmbChinaDebitCardTransFlow> findByTaskid(String taskid);

}
