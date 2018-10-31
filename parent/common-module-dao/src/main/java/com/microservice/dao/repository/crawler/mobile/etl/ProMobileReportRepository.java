package com.microservice.dao.repository.crawler.mobile.etl;

import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.microservice.dao.entity.crawler.mobile.etl.ProMobileReport;


public interface ProMobileReportRepository extends  CrudRepository<ProMobileReport, Long>{

	@Procedure(name = "pro_mobile_report")
    String proMobileReport(@Param("taskid") String taskid);
	
	@Procedure(name = "cmcc_mobile_etl")
    String cmccMobileEtl(@Param("taskid") String taskid);
	
	@Procedure(name = "unicom_mobile_etl")
    String unicomMobileEtl(@Param("taskid") String taskid);
	
	@Procedure(name = "telecom_jiangsu_etl")
    String telecomJiangsuEtl(@Param("taskid") String taskid);
	
	@Procedure(name = "telecom_chongqing_etl")
    String telecomChongqingEtl(@Param("taskid") String taskid);
	
	@Procedure(name = "telecom_shanxi1_etl")
    String telecomShanxi1Etl(@Param("taskid") String taskid);
	
	@Procedure(name = "telecom_guangdong_etl")
    String telecomGuangdongEtl(@Param("taskid") String taskid);
	
	@Procedure(name = "telecom_tianjin_etl")
    String telecomTianjinEtl(@Param("taskid") String taskid);
	
	@Procedure(name = "telecom_hunan_etl")
    String telecomHunanEtl(@Param("taskid") String taskid);
	
	@Procedure(name = "telecom_hebei_etl")
    String telecomHebeiEtl(@Param("taskid") String taskid);
	
	@Procedure(name = "telecom_anhui_etl")
    String telecomAnhuiEtl(@Param("taskid") String taskid);
	
	@Procedure(name = "telecom_hainan_etl")
    String telecomHainanEtl(@Param("taskid") String taskid);
	
	@Procedure(name = "telecom_qinghai_etl")
    String telecomQinghaiEtl(@Param("taskid") String taskid);
	
	@Procedure(name = "telecom_xinjiang_etl")
    String telecomXinjiangEtl(@Param("taskid") String taskid);
	
	@Procedure(name = "telecom_fujian_etl")
    String telecomFujianEtl(@Param("taskid") String taskid);
	
	@Procedure(name = "telecom_jilin_etl")
    String telecomJilinEtl(@Param("taskid") String taskid);
	
	@Procedure(name = "telecom_shanghai_etl")
    String telecomShanghaiEtl(@Param("taskid") String taskid);
	
	@Procedure(name = "telecom_henan_etl")
    String telecomHenanEtl(@Param("taskid") String taskid);
	
	@Procedure(name = "telecom_ningxia_etl")
    String telecomNingxiaEtl(@Param("taskid") String taskid);
	
	@Procedure(name = "telecom_sichuan_etl")
    String telecomSichuanEtl(@Param("taskid") String taskid);
	
	@Procedure(name = "telecom_zhejiang_etl")
    String telecomZhejiangEtl(@Param("taskid") String taskid);
	
	@Procedure(name = "telecom_shandong_etl")
    String telecomShandongEtl(@Param("taskid") String taskid);
	
	@Procedure(name = "telecom_jiangxi_etl")
    String telecomJiangxiEtl(@Param("taskid") String taskid);
	
	@Procedure(name = "telecom_guizhou_etl")
    String telecomGuizhouEtl(@Param("taskid") String taskid);
	
	@Procedure(name = "telecom_liaoning_etl")
    String telecomLiaoningEtl(@Param("taskid") String taskid);
	
	@Procedure(name = "telecom_beijing_etl")
    String telecomBeijingEtl(@Param("taskid") String taskid);
	
	@Procedure(name = "telecom_yunnan_etl")
    String telecomYunnanEtl(@Param("taskid") String taskid);
	
	@Procedure(name = "telecom_shanxi3_etl")
    String telecomShanxi3Etl(@Param("taskid") String taskid);
	
	@Procedure(name = "telecom_neimenggu_etl")
    String telecomNeimengguEtl(@Param("taskid") String taskid);
	
	@Procedure(name = "telecom_gansu_etl")
    String telecomGansuEtl(@Param("taskid") String taskid);
	
	@Procedure(name = "telecom_hubei_etl")
    String telecomHubeiEtl(@Param("taskid") String taskid);
	
	@Procedure(name = "telecom_guangxi_etl")
    String telecomGuangxiEtl(@Param("taskid") String taskid);
	
	@Procedure(name = "telecom_heilongjiang_etl")
    String telecomHeilongjiangEtl(@Param("taskid") String taskid);
	
	
}
