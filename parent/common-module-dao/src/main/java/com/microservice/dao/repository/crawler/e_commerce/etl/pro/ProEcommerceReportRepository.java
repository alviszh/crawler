package com.microservice.dao.repository.crawler.e_commerce.etl.pro;

import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.microservice.dao.entity.crawler.e_commerce.etl.pro.ProEcommerceReport;


public interface ProEcommerceReportRepository extends  CrudRepository<ProEcommerceReport, Long>{

	@Procedure(name = "pro_mobile_report")
    String proEcommerceReport(@Param("taskid") String taskid);
	
	@Procedure(name = "pro_ecommerce_taobao_etl")
    String taobaoEtl(@Param("taskid") String taskid);
	
	@Procedure(name = "pro_ecommerce_jd_etl")
    String jdEtl(@Param("taskid") String taskid);
	
	@Procedure(name = "pro_ecommerce_alipay_etl")
    String alipayEtl(@Param("taskid") String taskid);
	
	@Procedure(name = "pro_ecommerce_suning_etl")
    String snEtl(@Param("taskid") String taskid);
	
}
