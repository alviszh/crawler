package app.service;

import app.commontracerlog.TracerLog;
import app.service.aop.InsuranceLogin;
import com.crawler.insurance.json.InsuranceRequestParameters;
import com.crawler.insurance.json.InsuranceStatusCode;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.insurance.basic","com.microservice.dao.entity.crawler.insurance.shenyang"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.insurance.basic","com.microservice.dao.repository.crawler.insurance.shenyang"})
public class InsuranceShenyangService implements InsuranceLogin {

    @Autowired
    private InsuranceShenyangPensionService insuranceShenyangPensionService;
    @Autowired
    private InsuranceShenyangUnemployeedService insuranceShenyangUnemployeedService;
    @Autowired
    private TaskInsuranceRepository taskInsuranceRepository;
    @Autowired
    private TracerLog tracer;

    @Override
    public TaskInsurance login(InsuranceRequestParameters insuranceRequestParameters) {
        //设置状态登录中，status值为0，养老，失业，医疗有一个登陆成功则递加1，若任意一个失败则置为4;
        String medicalNum = insuranceRequestParameters.getVerification();
        AtomicInteger status = new AtomicInteger(0);
        //需要登录的数目
        AtomicInteger loginNum = new AtomicInteger(2);
        if(medicalNum != null && !medicalNum.trim().equals("") )
            loginNum = new AtomicInteger(3);

        TaskInsurance taskInsurance = insuranceShenyangPensionService.changeStatus(insuranceRequestParameters,medicalNum);
        System.out.println("\n\nshenyang-controller-login:"+taskInsurance);
        try {
            insuranceShenyangPensionService.login(insuranceRequestParameters,1,status,loginNum);
            insuranceShenyangUnemployeedService.login(insuranceRequestParameters,1,status,loginNum);
            if (loginNum.get() == 3){//有医保号码则登陆
                //insuranceShenyangMedicalService.login(insuranceRequestParameters,1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return taskInsurance;
    }

    @Override
    public TaskInsurance getAllData(InsuranceRequestParameters insuranceRequestParameters) {
        tracer.qryKeyValue("taskid",insuranceRequestParameters.getTaskId());
        tracer.qryKeyValue("username",insuranceRequestParameters.getUsername());
        String medicalNum = insuranceRequestParameters.getVerification();
        //需要爬取的数目
        AtomicInteger loginNum = new AtomicInteger(2);
        if(medicalNum != null && !medicalNum.trim().equals("") )
            loginNum = new AtomicInteger(3);
        TaskInsurance taskInsurance =  insuranceShenyangPensionService.getTaskInsurance(insuranceRequestParameters);
        //更改状态完正在爬取
        if(loginNum.get() == 3) {
            //insuranceShenyangMedicalService.getMedicalInfo(insuranceRequestParameters);
        }
        insuranceShenyangPensionService.changeCrawlerStatusDoing(insuranceRequestParameters);
        try {
            insuranceShenyangPensionService.getUserInfo(insuranceRequestParameters);
        } catch (Exception e) {
            tracer.addTag("用户数据爬取失败", e.getMessage());
            taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_BASE_INFO_FAILUE.getDescription());
            taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_BASE_INFO_FAILUE.getPhase());
            taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_CRAWLER_BASE_INFO_FAILUE.getPhasestatus());
            taskInsurance = taskInsuranceRepository.save(taskInsurance);
        }
        try {
            insuranceShenyangUnemployeedService.getUnemployeedInfo(insuranceRequestParameters);
        } catch (Exception e) {
           tracer.addTag("失业保险爬取失败", e.getMessage());
            taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_UNEMPLOYMENT_FAILUE.getDescription());
            taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_UNEMPLOYMENT_FAILUE.getPhase());
            taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_CRAWLER_UNEMPLOYMENT_FAILUE.getPhasestatus());
            taskInsurance = taskInsuranceRepository.save(taskInsurance);
        }
        return taskInsurance;
    }

    @Override
    public TaskInsurance getAllDataDone(String taskId) {
        return null;
    }
}
