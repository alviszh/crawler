package app.service;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;

import app.commontracerlog.TracerLog;
import app.service.aop.InsuranceCrawler;
import app.service.aop.InsuranceLogin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @author zhangyongjie
 * @create 2017-09-19 15:56
 * @Desc 异步爬取数据
 */
@Component
public class AsyncQuanzhouGetAllDataService extends InsuranceService implements InsuranceCrawler{

    @Autowired
    private TracerLog tracer;
    @Autowired
    private InsuranceQuanzhouService insuranceQuanzhouService;


    /**
     * @Des 爬取总方法
     * @param insuranceRequestParameters
     * @throws Exception
     */
    @Async
    @Override
	public TaskInsurance getAllData(InsuranceRequestParameters insuranceRequestParameters) {
		TaskInsurance taskInsurance = insuranceQuanzhouService.updateTaskInsurance(insuranceRequestParameters);
        tracer.addTag("AsyncQuanzhouGetAllDataService.crawler.getAllData", insuranceRequestParameters.getTaskId());

        tracer.addTag("parser.crawler.taskid",insuranceRequestParameters.getTaskId());
        tracer.addTag("parser.crawler.auth",insuranceRequestParameters.getUsername());
        //获取用户基本信息
        try {
            insuranceQuanzhouService.getUserInfo(insuranceRequestParameters);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //获取企业职工基本养老保险
        try {
            insuranceQuanzhouService.getEmpPension(insuranceRequestParameters);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //城镇职工基本医疗保险
        try {
            insuranceQuanzhouService.getEmpMedical(insuranceRequestParameters);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //工伤保险
        try {
            insuranceQuanzhouService.getInjury(insuranceRequestParameters);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //生育保险
        try {
            insuranceQuanzhouService.getBirth(insuranceRequestParameters);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //暂无失业保险参保信息和缴费信息，直接修改爬取状态为成功
        try {
            insuranceQuanzhouService.getUnemployment(insuranceRequestParameters);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        taskInsurance = changeCrawlerStatusSuccess(taskInsurance.getTaskid());
        return taskInsurance;
    }




	@Override
	public TaskInsurance getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}


}
