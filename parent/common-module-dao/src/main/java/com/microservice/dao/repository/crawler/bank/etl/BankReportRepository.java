package com.microservice.dao.repository.crawler.bank.etl;

import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.microservice.dao.entity.crawler.bank.etl.ProBankReport;


public interface BankReportRepository extends  CrudRepository<ProBankReport, Long>{

	@Procedure(name = "pro_abc_credit_etl")
    String pro_abc_credit_etl(@Param("taskid") String taskid);
	
	@Procedure(name = "pro_abc_debit_etl")
	String pro_abc_debit_etl(@Param("taskid") String taskid);
	
	@Procedure(name = "pro_bob_debit_etl")
	String pro_bob_debit_etl(@Param("taskid") String taskid);
	
	@Procedure(name = "pro_boc_credit_etl")
	String pro_boc_credit_etl(@Param("taskid") String taskid);
	
	@Procedure(name = "pro_boc_debit_etl")
	String pro_boc_debit_etl(@Param("taskid") String taskid);
	
	@Procedure(name = "pro_bocom_debit_etl")
	String pro_bocom_debit_etl(@Param("taskid") String taskid);
	
	@Procedure(name = "pro_bocom_credit_etl")
	String pro_bocom_credit_etl(@Param("taskid") String taskid);
	
	@Procedure(name = "pro_bohc_debit_etl")
	String pro_bohc_debit_etl(@Param("taskid") String taskid);
	
	@Procedure(name = "pro_ccb_credit_etl")
	String pro_ccb_credit_etl(@Param("taskid") String taskid);
	
	@Procedure(name = "pro_ccb_debit_etl")
	String pro_ccb_debit_etl(@Param("taskid") String taskid);
	
	@Procedure(name = "pro_ceb_debit_etl")
	String pro_ceb_debit_etl(@Param("taskid") String taskid);
	
	@Procedure(name = "pro_ceb_credit_etl")
	String pro_ceb_credit_etl(@Param("taskid") String taskid);
	
	@Procedure(name = "pro_cgb_credit_etl")
	String pro_cgb_credit_etl(@Param("taskid") String taskid);
	
	@Procedure(name = "pro_cgb_debit_etl")
	String pro_cgb_debit_etl(@Param("taskid") String taskid);
	
	@Procedure(name = "pro_cib_credit_etl")
	String pro_cib_credit_etl(@Param("taskid") String taskid);
	
	@Procedure(name = "pro_cib_debit_etl")
	String pro_cib_debit_etl(@Param("taskid") String taskid);
	
	@Procedure(name = "pro_citic_debit_etl")
	String pro_citic_debit_etl(@Param("taskid") String taskid);
	
	@Procedure(name = "pro_citic_credit_etl")
	String pro_citic_credit_etl(@Param("taskid") String taskid);
	
	@Procedure(name = "pro_cmbc_credit_etl")
	String pro_cmbc_credit_etl(@Param("taskid") String taskid);
	
	@Procedure(name = "pro_cmbc_debit_etl")
	String pro_cmbc_debit_etl(@Param("taskid") String taskid);
	
	@Procedure(name = "pro_cmb_credit_etl")
	String pro_cmb_credit_etl(@Param("taskid") String taskid);
	
	@Procedure(name = "pro_cmb_debit_etl")
	String pro_cmb_debit_etl(@Param("taskid") String taskid);
	
	@Procedure(name = "pro_czb_debit_etl")
	String pro_czb_debit_etl(@Param("taskid") String taskid);
	
	@Procedure(name = "pro_hfb_debit_etl")
	String pro_hfb_debit_etl(@Param("taskid") String taskid);
	
	@Procedure(name = "pro_hxb_credit_etl")
	String pro_hxb_credit_etl(@Param("taskid") String taskid);
	
	@Procedure(name = "pro_hxb_debit_etl")
	String pro_hxb_debit_etl(@Param("taskid") String taskid);
	
	@Procedure(name = "pro_icbc_credit_etl")
	String pro_icbc_credit_etl(@Param("taskid") String taskid);
	
	@Procedure(name = "pro_icbc_debit_etl")
	String pro_icbc_debit_etl(@Param("taskid") String taskid);
	
	@Procedure(name = "pro_pab_credit_etl")
	String pro_pab_credit_etl(@Param("taskid") String taskid);
	
	@Procedure(name = "pro_pab_debit_etl")
	String pro_pab_debit_etl(@Param("taskid") String taskid);
	
	@Procedure(name = "pro_psb_debit_etl")
	String pro_psb_debit_etl(@Param("taskid") String taskid);
	
	@Procedure(name = "pro_spdb_debit_etl")
	String pro_spdb_debit_etl(@Param("taskid") String taskid);
	
	@Procedure(name = "pro_spdb_credit_etl")
	String pro_spdb_credit_etl(@Param("taskid") String taskid);
	
	
}
