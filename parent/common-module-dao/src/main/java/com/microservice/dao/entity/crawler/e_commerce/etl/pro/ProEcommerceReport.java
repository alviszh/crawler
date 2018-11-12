package com.microservice.dao.entity.crawler.e_commerce.etl.pro;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.NamedStoredProcedureQueries;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureParameter;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name = "pro_e_commerce_report")
@NamedStoredProcedureQueries({
		@NamedStoredProcedureQuery(name = "pro_ecommerce_jd_etl", procedureName = "public.pro_ecommerce_jd_etl", parameters = {
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "taskid", type = String.class),
				@StoredProcedureParameter(mode = ParameterMode.OUT, name = "result", type = String.class) }),
		@NamedStoredProcedureQuery(name = "pro_ecommerce_suning_etl", procedureName = "public.pro_ecommerce_suning_etl", parameters = {
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "taskid", type = String.class),
				@StoredProcedureParameter(mode = ParameterMode.OUT, name = "result", type = String.class) }),
		@NamedStoredProcedureQuery(name = "pro_ecommerce_taobao_etl", procedureName = "public.pro_ecommerce_taobao_etl", parameters = {
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "taskid", type = String.class),
				@StoredProcedureParameter(mode = ParameterMode.OUT, name = "result", type = String.class) }),
		@NamedStoredProcedureQuery(name = "pro_ecommerce_alipay_etl", procedureName = "public.pro_ecommerce_alipay_etl", parameters = {
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "taskid", type = String.class),
				@StoredProcedureParameter(mode = ParameterMode.OUT, name = "result", type = String.class) })
		}) 
public class ProEcommerceReport extends IdEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2283476239936109239L;
	
	
	

}
