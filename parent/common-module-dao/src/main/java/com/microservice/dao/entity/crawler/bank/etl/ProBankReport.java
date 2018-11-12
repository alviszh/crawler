package com.microservice.dao.entity.crawler.bank.etl;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.NamedStoredProcedureQueries;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureParameter;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name = "pro_bank_report")
@NamedStoredProcedureQueries({
		@NamedStoredProcedureQuery(name = "pro_abc_credit_etl", procedureName = "public.pro_abc_credit_etl", parameters = {
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "taskid", type = String.class),
				@StoredProcedureParameter(mode = ParameterMode.OUT, name = "result", type = String.class) }),
		@NamedStoredProcedureQuery(name = "pro_abc_debit_etl", procedureName = "public.pro_abc_debit_etl", parameters = {
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "taskid", type = String.class),
				@StoredProcedureParameter(mode = ParameterMode.OUT, name = "result", type = String.class) }),
		@NamedStoredProcedureQuery(name = "pro_bob_debit_etl", procedureName = "public.pro_bob_debit_etl", parameters = {
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "taskid", type = String.class),
				@StoredProcedureParameter(mode = ParameterMode.OUT, name = "result", type = String.class) }),
		@NamedStoredProcedureQuery(name = "pro_boc_credit_etl", procedureName = "public.pro_boc_credit_etl", parameters = {
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "taskid", type = String.class),
				@StoredProcedureParameter(mode = ParameterMode.OUT, name = "result", type = String.class) }),
		@NamedStoredProcedureQuery(name = "pro_boc_debit_etl", procedureName = "public.pro_boc_debit_etl", parameters = {
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "taskid", type = String.class),
				@StoredProcedureParameter(mode = ParameterMode.OUT, name = "result", type = String.class) }),
		@NamedStoredProcedureQuery(name = "pro_bocom_debit_etl", procedureName = "public.pro_bocom_debit_etl", parameters = {
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "taskid", type = String.class),
				@StoredProcedureParameter(mode = ParameterMode.OUT, name = "result", type = String.class) }),
		@NamedStoredProcedureQuery(name = "pro_bocom_credit_etl", procedureName = "public.pro_bocom_credit_etl", parameters = {
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "taskid", type = String.class),
				@StoredProcedureParameter(mode = ParameterMode.OUT, name = "result", type = String.class) }),
		@NamedStoredProcedureQuery(name = "pro_bohc_debit_etl", procedureName = "public.pro_bohc_debit_etl", parameters = {
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "taskid", type = String.class),
				@StoredProcedureParameter(mode = ParameterMode.OUT, name = "result", type = String.class) }),
		@NamedStoredProcedureQuery(name = "pro_ccb_credit_etl", procedureName = "public.pro_ccb_credit_etl", parameters = {
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "taskid", type = String.class),
				@StoredProcedureParameter(mode = ParameterMode.OUT, name = "result", type = String.class) }),
		@NamedStoredProcedureQuery(name = "pro_ccb_debit_etl", procedureName = "public.pro_ccb_debit_etl", parameters = {
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "taskid", type = String.class),
				@StoredProcedureParameter(mode = ParameterMode.OUT, name = "result", type = String.class) }),
		@NamedStoredProcedureQuery(name = "pro_ceb_debit_etl", procedureName = "public.pro_ceb_debit_etl", parameters = {
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "taskid", type = String.class),
				@StoredProcedureParameter(mode = ParameterMode.OUT, name = "result", type = String.class) }),
		@NamedStoredProcedureQuery(name = "pro_ceb_credit_etl", procedureName = "public.pro_ceb_credit_etl", parameters = {
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "taskid", type = String.class),
				@StoredProcedureParameter(mode = ParameterMode.OUT, name = "result", type = String.class) }),
		@NamedStoredProcedureQuery(name = "pro_cgb_credit_etl", procedureName = "public.pro_cgb_credit_etl", parameters = {
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "taskid", type = String.class),
				@StoredProcedureParameter(mode = ParameterMode.OUT, name = "result", type = String.class) }),
		@NamedStoredProcedureQuery(name = "pro_cgb_debit_etl", procedureName = "public.pro_cgb_debit_etl", parameters = {
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "taskid", type = String.class),
				@StoredProcedureParameter(mode = ParameterMode.OUT, name = "result", type = String.class) }),
		@NamedStoredProcedureQuery(name = "pro_cib_credit_etl", procedureName = "public.pro_cib_credit_etl", parameters = {
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "taskid", type = String.class),
				@StoredProcedureParameter(mode = ParameterMode.OUT, name = "result", type = String.class) }),
		@NamedStoredProcedureQuery(name = "pro_cib_debit_etl", procedureName = "public.pro_cib_debit_etl", parameters = {
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "taskid", type = String.class),
				@StoredProcedureParameter(mode = ParameterMode.OUT, name = "result", type = String.class) }),
		@NamedStoredProcedureQuery(name = "pro_citic_debit_etl", procedureName = "public.pro_citic_debit_etl", parameters = {
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "taskid", type = String.class),
				@StoredProcedureParameter(mode = ParameterMode.OUT, name = "result", type = String.class) }),
		@NamedStoredProcedureQuery(name = "pro_citic_credit_etl", procedureName = "public.pro_citic_credit_etl", parameters = {
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "taskid", type = String.class),
				@StoredProcedureParameter(mode = ParameterMode.OUT, name = "result", type = String.class) }),
		@NamedStoredProcedureQuery(name = "pro_cmbc_credit_etl", procedureName = "public.pro_cmbc_credit_etl", parameters = {
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "taskid", type = String.class),
				@StoredProcedureParameter(mode = ParameterMode.OUT, name = "result", type = String.class) }),
		@NamedStoredProcedureQuery(name = "pro_cmbc_debit_etl", procedureName = "public.pro_cmbc_debit_etl", parameters = {
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "taskid", type = String.class),
				@StoredProcedureParameter(mode = ParameterMode.OUT, name = "result", type = String.class) }),
		@NamedStoredProcedureQuery(name = "pro_cmb_credit_etl", procedureName = "public.pro_cmb_credit_etl", parameters = {
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "taskid", type = String.class),
				@StoredProcedureParameter(mode = ParameterMode.OUT, name = "result", type = String.class) }),
		@NamedStoredProcedureQuery(name = "pro_cmb_debit_etl", procedureName = "public.pro_cmb_debit_etl", parameters = {
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "taskid", type = String.class),
				@StoredProcedureParameter(mode = ParameterMode.OUT, name = "result", type = String.class) }),
		@NamedStoredProcedureQuery(name = "pro_czb_debit_etl", procedureName = "public.pro_czb_debit_etl", parameters = {
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "taskid", type = String.class),
				@StoredProcedureParameter(mode = ParameterMode.OUT, name = "result", type = String.class) }),
		@NamedStoredProcedureQuery(name = "pro_hfb_debit_etl", procedureName = "public.pro_hfb_debit_etl", parameters = {
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "taskid", type = String.class),
				@StoredProcedureParameter(mode = ParameterMode.OUT, name = "result", type = String.class) }),
		@NamedStoredProcedureQuery(name = "pro_hxb_credit_etl", procedureName = "public.pro_hxb_credit_etl", parameters = {
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "taskid", type = String.class),
				@StoredProcedureParameter(mode = ParameterMode.OUT, name = "result", type = String.class) }),
		@NamedStoredProcedureQuery(name = "pro_hxb_debit_etl", procedureName = "public.pro_hxb_debit_etl", parameters = {
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "taskid", type = String.class),
				@StoredProcedureParameter(mode = ParameterMode.OUT, name = "result", type = String.class) }),
		@NamedStoredProcedureQuery(name = "pro_icbc_credit_etl", procedureName = "public.pro_icbc_credit_etl", parameters = {
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "taskid", type = String.class),
				@StoredProcedureParameter(mode = ParameterMode.OUT, name = "result", type = String.class) }),
		@NamedStoredProcedureQuery(name = "pro_icbc_debit_etl", procedureName = "public.pro_icbc_debit_etl", parameters = {
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "taskid", type = String.class),
				@StoredProcedureParameter(mode = ParameterMode.OUT, name = "result", type = String.class) }),
		@NamedStoredProcedureQuery(name = "pro_pab_credit_etl", procedureName = "public.pro_pab_credit_etl", parameters = {
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "taskid", type = String.class),
				@StoredProcedureParameter(mode = ParameterMode.OUT, name = "result", type = String.class) }),
		@NamedStoredProcedureQuery(name = "pro_pab_debit_etl", procedureName = "public.pro_pab_debit_etl", parameters = {
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "taskid", type = String.class),
				@StoredProcedureParameter(mode = ParameterMode.OUT, name = "result", type = String.class) }),
		@NamedStoredProcedureQuery(name = "pro_psb_debit_etl", procedureName = "public.pro_psb_debit_etl", parameters = {
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "taskid", type = String.class),
				@StoredProcedureParameter(mode = ParameterMode.OUT, name = "result", type = String.class) }),
		@NamedStoredProcedureQuery(name = "pro_spdb_debit_etl", procedureName = "public.pro_spdb_debit_etl", parameters = {
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "taskid", type = String.class),
				@StoredProcedureParameter(mode = ParameterMode.OUT, name = "result", type = String.class) }),
		@NamedStoredProcedureQuery(name = "pro_spdb_credit_etl", procedureName = "public.pro_spdb_credit_etl", parameters = {
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "taskid", type = String.class),
				@StoredProcedureParameter(mode = ParameterMode.OUT, name = "result", type = String.class) })
})
public class ProBankReport extends IdEntity implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2283476239936109239L;

}
