package app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;

import app.commontracerlog.TracerLog;

/**
 * @author zhangyongjie
 * @create 2017-09-22 18:36
 * @Desc 异步爬取数据
 */
@Component
public class AsyncWuxiGetAllDataService {
    @Autowired
    private TracerLog tracer;
    @Autowired
    private InsuranceWuxiService insuranceWuxiService;
    @Autowired
    private TaskInsuranceRepository taskInsuranceRepository;

    /**
     * @Des 爬取总方法
     * @param insuranceRequestParameters
     * @throws Exception
     */
    @Async
    public void getAllData(InsuranceRequestParameters insuranceRequestParameters) throws Exception {
        tracer.addTag("AsyncWuxiGetAllDataService.crawler.getAllData", insuranceRequestParameters.getTaskId());

        tracer.addTag("parser.crawler.taskid",insuranceRequestParameters.getTaskId());
        tracer.addTag("parser.crawler.auth",insuranceRequestParameters.getUsername());
        //获取用户基本信息
        try {
            insuranceWuxiService.getUserInfo(insuranceRequestParameters);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //获取用户参保信息
        try {
            insuranceWuxiService.getInsuredInfo(insuranceRequestParameters,1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //获取用户医疗保险信息
        try {
            insuranceWuxiService.getMedical(insuranceRequestParameters,1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //暂无养老保险信息访问权限
        try {
            insuranceWuxiService.getPension(insuranceRequestParameters);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //暂无工伤保险信息访问权限
        try {
            insuranceWuxiService.getInjury(insuranceRequestParameters);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //暂无生育保险信息访问权限
        try {
            insuranceWuxiService.getBirth(insuranceRequestParameters);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //暂无失业保险信息访问权限
        try {
            insuranceWuxiService.getUnemployment(insuranceRequestParameters);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
