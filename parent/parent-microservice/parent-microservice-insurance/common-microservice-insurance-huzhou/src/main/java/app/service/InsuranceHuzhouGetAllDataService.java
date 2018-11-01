package app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.crawler.insurance.json.InsuranceRequestParameters;

import app.commontracerlog.TracerLog;

@Component
public class InsuranceHuzhouGetAllDataService {
	@Autowired
	private InsuranceHuzhouRecordsService insuranceHuzhouRecordsService;
	@Autowired
	private TracerLog tracer;	

	/**
	 * @Des 爬取总方法
	 * @param insuranceRequestParameters
	 * @throws Exception 
	 */
	public void getAllData(InsuranceRequestParameters insuranceRequestParameters) throws Exception{
		tracer.addTag("getAllData", insuranceRequestParameters.getTaskId());
		insuranceHuzhouRecordsService.getInsuranceRecords(insuranceRequestParameters, "基本养老保险");
		insuranceHuzhouRecordsService.getInsuranceRecords(insuranceRequestParameters, "失业保险");
		insuranceHuzhouRecordsService.getInsuranceRecords(insuranceRequestParameters, "工伤保险");
		insuranceHuzhouRecordsService.getInsuranceRecords(insuranceRequestParameters, "生育保险");
		insuranceHuzhouRecordsService.getInsuranceRecords(insuranceRequestParameters, "基本医疗保险");
		insuranceHuzhouRecordsService.getInsuranceRecords(insuranceRequestParameters, "门诊统筹个人部分");
		insuranceHuzhouRecordsService.getInsuranceRecords(insuranceRequestParameters, "门诊统筹单位部分");
	}
}
