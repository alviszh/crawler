package com.microservice.dao.entity.crawler.mobile.etl;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.NamedStoredProcedureQueries;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureParameter;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name = "pro_mobile_report")
@NamedStoredProcedureQueries({
		@NamedStoredProcedureQuery(name = "pro_mobile_report", procedureName = "public.pro_mobile_report", parameters = {
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "taskid", type = String.class),
				@StoredProcedureParameter(mode = ParameterMode.OUT, name = "result", type = String.class) }),
		@NamedStoredProcedureQuery(name = "cmcc_mobile_etl", procedureName = "public.cmcc_mobile_etl", parameters = {
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "taskid", type = String.class),
				@StoredProcedureParameter(mode = ParameterMode.OUT, name = "result", type = String.class) }),
		@NamedStoredProcedureQuery(name = "unicom_mobile_etl", procedureName = "public.unicom_mobile_etl", parameters = {
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "taskid", type = String.class),
				@StoredProcedureParameter(mode = ParameterMode.OUT, name = "result", type = String.class) }),
		@NamedStoredProcedureQuery(name = "telecom_jiangsu_etl", procedureName = "public.telecom_jiangsu_etl", parameters = {
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "taskid", type = String.class),
				@StoredProcedureParameter(mode = ParameterMode.OUT, name = "result", type = String.class) }),
		@NamedStoredProcedureQuery(name = "telecom_chongqing_etl", procedureName = "public.telecom_chongqing_etl", parameters = {
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "taskid", type = String.class),
				@StoredProcedureParameter(mode = ParameterMode.OUT, name = "result", type = String.class) }),
		@NamedStoredProcedureQuery(name = "telecom_shanxi1_etl", procedureName = "public.telecom_shanxi1_etl", parameters = {
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "taskid", type = String.class),
				@StoredProcedureParameter(mode = ParameterMode.OUT, name = "result", type = String.class) }),
		@NamedStoredProcedureQuery(name = "telecom_guangdong_etl", procedureName = "public.telecom_guangdong_etl", parameters = {
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "taskid", type = String.class),
				@StoredProcedureParameter(mode = ParameterMode.OUT, name = "result", type = String.class) }),
		@NamedStoredProcedureQuery(name = "telecom_tianjin_etl", procedureName = "public.telecom_tianjin_etl", parameters = {
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "taskid", type = String.class),
				@StoredProcedureParameter(mode = ParameterMode.OUT, name = "result", type = String.class) }),
		@NamedStoredProcedureQuery(name = "telecom_hunan_etl", procedureName = "public.telecom_hunan_etl", parameters = {
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "taskid", type = String.class),
				@StoredProcedureParameter(mode = ParameterMode.OUT, name = "result", type = String.class) }),
		@NamedStoredProcedureQuery(name = "telecom_hebei_etl", procedureName = "public.telecom_hebei_etl", parameters = {
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "taskid", type = String.class),
				@StoredProcedureParameter(mode = ParameterMode.OUT, name = "result", type = String.class) }),
		@NamedStoredProcedureQuery(name = "telecom_anhui_etl", procedureName = "public.telecom_anhui_etl", parameters = {
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "taskid", type = String.class),
				@StoredProcedureParameter(mode = ParameterMode.OUT, name = "result", type = String.class) }),
		@NamedStoredProcedureQuery(name = "telecom_hainan_etl", procedureName = "public.telecom_hainan_etl", parameters = {
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "taskid", type = String.class),
				@StoredProcedureParameter(mode = ParameterMode.OUT, name = "result", type = String.class) }),
		@NamedStoredProcedureQuery(name = "telecom_qinghai_etl", procedureName = "public.telecom_qinghai_etl", parameters = {
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "taskid", type = String.class),
				@StoredProcedureParameter(mode = ParameterMode.OUT, name = "result", type = String.class) }),
		@NamedStoredProcedureQuery(name = "telecom_xinjiang_etl", procedureName = "public.telecom_xinjiang_etl", parameters = {
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "taskid", type = String.class),
				@StoredProcedureParameter(mode = ParameterMode.OUT, name = "result", type = String.class) }),
		@NamedStoredProcedureQuery(name = "telecom_fujian_etl", procedureName = "public.telecom_fujian_etl", parameters = {
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "taskid", type = String.class),
				@StoredProcedureParameter(mode = ParameterMode.OUT, name = "result", type = String.class) }),
		@NamedStoredProcedureQuery(name = "telecom_jilin_etl", procedureName = "public.telecom_jilin_etl", parameters = {
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "taskid", type = String.class),
				@StoredProcedureParameter(mode = ParameterMode.OUT, name = "result", type = String.class) }),
		@NamedStoredProcedureQuery(name = "telecom_shanghai_etl", procedureName = "public.telecom_shanghai_etl", parameters = {
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "taskid", type = String.class),
				@StoredProcedureParameter(mode = ParameterMode.OUT, name = "result", type = String.class) }),
		@NamedStoredProcedureQuery(name = "telecom_henan_etl", procedureName = "public.telecom_henan_etl", parameters = {
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "taskid", type = String.class),
				@StoredProcedureParameter(mode = ParameterMode.OUT, name = "result", type = String.class) }),
		@NamedStoredProcedureQuery(name = "telecom_ningxia_etl", procedureName = "public.telecom_ningxia_etl", parameters = {
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "taskid", type = String.class),
				@StoredProcedureParameter(mode = ParameterMode.OUT, name = "result", type = String.class) }),
		@NamedStoredProcedureQuery(name = "telecom_sichuan_etl", procedureName = "public.telecom_sichuan_etl", parameters = {
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "taskid", type = String.class),
				@StoredProcedureParameter(mode = ParameterMode.OUT, name = "result", type = String.class) }),
		@NamedStoredProcedureQuery(name = "telecom_zhejiang_etl", procedureName = "public.telecom_zhejiang_etl", parameters = {
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "taskid", type = String.class),
				@StoredProcedureParameter(mode = ParameterMode.OUT, name = "result", type = String.class) }),
		@NamedStoredProcedureQuery(name = "telecom_shandong_etl", procedureName = "public.telecom_shandong_etl", parameters = {
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "taskid", type = String.class),
				@StoredProcedureParameter(mode = ParameterMode.OUT, name = "result", type = String.class) }),
		@NamedStoredProcedureQuery(name = "telecom_jiangxi_etl", procedureName = "public.telecom_jiangxi_etl", parameters = {
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "taskid", type = String.class),
				@StoredProcedureParameter(mode = ParameterMode.OUT, name = "result", type = String.class) }),
		@NamedStoredProcedureQuery(name = "telecom_guizhou_etl", procedureName = "public.telecom_guizhou_etl", parameters = {
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "taskid", type = String.class),
				@StoredProcedureParameter(mode = ParameterMode.OUT, name = "result", type = String.class) }),
		@NamedStoredProcedureQuery(name = "telecom_liaoning_etl", procedureName = "public.telecom_liaoning_etl", parameters = {
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "taskid", type = String.class),
				@StoredProcedureParameter(mode = ParameterMode.OUT, name = "result", type = String.class) }),
		@NamedStoredProcedureQuery(name = "telecom_beijing_etl", procedureName = "public.telecom_beijing_etl", parameters = {
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "taskid", type = String.class),
				@StoredProcedureParameter(mode = ParameterMode.OUT, name = "result", type = String.class) }),
		@NamedStoredProcedureQuery(name = "telecom_yunnan_etl", procedureName = "public.telecom_yunnan_etl", parameters = {
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "taskid", type = String.class),
				@StoredProcedureParameter(mode = ParameterMode.OUT, name = "result", type = String.class) }),
		@NamedStoredProcedureQuery(name = "telecom_shanxi3_etl", procedureName = "public.telecom_shanxi3_etl", parameters = {
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "taskid", type = String.class),
				@StoredProcedureParameter(mode = ParameterMode.OUT, name = "result", type = String.class) }),
		@NamedStoredProcedureQuery(name = "telecom_neimenggu_etl", procedureName = "public.telecom_neimenggu_etl", parameters = {
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "taskid", type = String.class),
				@StoredProcedureParameter(mode = ParameterMode.OUT, name = "result", type = String.class) }),
		@NamedStoredProcedureQuery(name = "telecom_gansu_etl", procedureName = "public.telecom_gansu_etl", parameters = {
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "taskid", type = String.class),
				@StoredProcedureParameter(mode = ParameterMode.OUT, name = "result", type = String.class) }),
		@NamedStoredProcedureQuery(name = "telecom_hubei_etl", procedureName = "public.telecom_hubei_etl", parameters = {
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "taskid", type = String.class),
				@StoredProcedureParameter(mode = ParameterMode.OUT, name = "result", type = String.class) }),
		@NamedStoredProcedureQuery(name = "telecom_guangxi_etl", procedureName = "public.telecom_guangxi_etl", parameters = {
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "taskid", type = String.class),
				@StoredProcedureParameter(mode = ParameterMode.OUT, name = "result", type = String.class) }),
		@NamedStoredProcedureQuery(name = "telecom_heilongjiang_etl", procedureName = "public.telecom_heilongjiang_etl", parameters = {
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "taskid", type = String.class),
				@StoredProcedureParameter(mode = ParameterMode.OUT, name = "result", type = String.class) })
		
		})
public class ProMobileReport extends IdEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2283476239936109239L;
	
	
	

}
