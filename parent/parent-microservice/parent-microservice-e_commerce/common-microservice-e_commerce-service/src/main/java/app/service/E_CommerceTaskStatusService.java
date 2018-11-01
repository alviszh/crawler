package app.service;

import java.util.Set;

import com.crawler.TaskStatusCode;
import com.microservice.dao.entity.crawler.e_commerce.basic.E_CommerceTask;
import com.microservice.dao.repository.crawler.e_commerce.basic.E_CommerceTaskRepository;

import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;

import com.crawler.e_commerce.json.E_ComerceStatusCode;
import com.crawler.e_commerce.json.E_CommerceJsonBean;
import com.crawler.microservice.unit.CommonUnit;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.google.gson.Gson;
import com.module.htmlunit.WebCrawler;

import app.commontracerlog.TracerLog;

/**
 * @author zz
 * @Des 更改task_bank表状态
 */
@Component
@EntityScan(basePackages = {"com.microservice.dao.entity.crawler.e_commerce.basic"})
@EnableJpaRepositories(basePackages = {"com.microservice.dao.repository.crawler.e_commerce.basic"})
public class E_CommerceTaskStatusService {

    @Autowired
    private TracerLog tracerLog;
    @Autowired
    private E_CommerceTaskRepository e_commerceTaskRepository;
    
    @Autowired
    private AgentService agentService;
    


    /**
     * @param phase
     * @param phasestatus
     * @param description
     * @param error_code
     * @param taskid
     * @return
     * @Des 改变task_bank状态
     * @author zz
     */
    public E_CommerceTask changeStatus(String phase, String phasestatus, String description, Integer error_code,
                                       boolean finished, String taskid) {
        tracerLog.output("change task status", description);

        E_CommerceTask e_commerceTask = e_commerceTaskRepository.findByTaskid(taskid);
        e_commerceTask.setPhase(phase);
        e_commerceTask.setPhase_status(phasestatus);
        e_commerceTask.setDescription(description);
        e_commerceTask.setError_code(error_code);
        if (finished) {
            e_commerceTask.setFinished(finished);
        }
        e_commerceTask = e_commerceTaskRepository.save(e_commerceTask);
        return e_commerceTask;
    }

    public E_CommerceTask changeStatus(String phase, String phasestatus, String description, Integer error_code,
                                       boolean finished, String taskid, String cookies) {
        tracerLog.output("change task status", description);

        E_CommerceTask e_commerceTask = e_commerceTaskRepository.findByTaskid(taskid);
        //taskBank.setCookies(cookies);
        e_commerceTask.setPhase(phase);
        e_commerceTask.setPhase_status(phasestatus);
        e_commerceTask.setDescription(description);
        e_commerceTask.setError_code(error_code);
        if (finished) {
            e_commerceTask.setFinished(finished);
        }
        e_commerceTaskRepository.save(e_commerceTask);
        return e_commerceTask;
    }
    
    /**
     * 
     * @param phase
     * @param phasestatus
     * @param description
     * @param error_code
     * @param type
     * @param code
     * @param taskid
     * @return
     * @Des 根据type改变E_CommerceTask状态以及相对应的爬取项的状态码
     * @author wpy
     */
    public E_CommerceTask changeStatusCode(String phase, String phasestatus, String description, Integer error_code,
            String type, Integer code, String taskid) {
		tracerLog.output("change task status", description);
		
		E_CommerceTask e_commerceTask = e_commerceTaskRepository.findByTaskid(taskid);
		e_commerceTask.setPhase(phase);
		e_commerceTask.setPhase_status(phasestatus);
		e_commerceTask.setDescription(description);
		e_commerceTask.setError_code(error_code);
		
		if(type.equals("userinfo")){
			e_commerceTask.setUserinfoStatus(code);
		}else if(type.equals("addressinfo")){
			e_commerceTask.setAddressInfoStatus(code);
		}else if(type.equals("bankcardinfo")){
			e_commerceTask.setBankCardInfoStatus(code);
		}else if(type.equals("orderinfo")){
			e_commerceTask.setOrderInfoStatus(code);
		}else if(type.equals("renzhenginfo")){
			e_commerceTask.setRenzhengInfoStatus(code);
		}else if(type.equals("privilegeinfo")){
			e_commerceTask.setBtPrivilegeInfoStatus(code);
		}else if(type.equals("alipayinfo")){
			e_commerceTask.setAlipayInfoStatus(code);
		}else if(type.equals("alipayment")){
			e_commerceTask.setAlipayPaymentInfoStatus(code);
		}
		
		e_commerceTask = e_commerceTaskRepository.save(e_commerceTask);
		return e_commerceTask;
	}

    /**
     * @param e_CommerceJsonBean
     * @return
     * @Des 登录准备
     */
    public E_CommerceTask changeStatusLoginDoing(E_CommerceJsonBean e_CommerceJsonBean) {
        E_CommerceTask  e_CommerceTask = e_commerceTaskRepository.findByTaskid(e_CommerceJsonBean.getTaskid());
        if (null == e_CommerceTask) {
            throw new RuntimeException("Entity bean E_CommerceTask is null ! taskid>>" + e_CommerceJsonBean.getTaskid() + "<<");
        }
        e_CommerceTask.setPhase(E_ComerceStatusCode.E_COMMERCE_LOGIN_DOING.getPhase());
        e_CommerceTask.setPhase_status(E_ComerceStatusCode.E_COMMERCE_LOGIN_DOING.getPhasestatus());
        e_CommerceTask.setDescription(E_ComerceStatusCode.E_COMMERCE_LOGIN_DOING.getDescription());
        e_CommerceTask.setError_code(E_ComerceStatusCode.E_COMMERCE_LOGIN_DOING.getError_code());
        Gson gson = new Gson();
        e_CommerceTask.setTesthtml(gson.toJson(e_CommerceJsonBean));
        //taskBank.setLoginType(bankJsonBean.getLoginType());
        e_CommerceTask.setLoginName(e_CommerceJsonBean.getUsername());
        
        e_CommerceTask.setCrawlerHost(e_CommerceJsonBean.getIp());
        e_CommerceTask.setCrawlerPort(e_CommerceJsonBean.getPort());
        e_CommerceTask = e_commerceTaskRepository.save(e_CommerceTask);
       
        return e_CommerceTask;
    }

    /**
     * @param <T>
     * @param driver
     * @param domain
     * @return
     * @Des 登录成功，cookie入库
     * @author zz
     */
    public <T> void transforCookie(WebDriver driver, String domain, E_CommerceTask e_commerceTask, T params) {

        WebClient webClient = WebCrawler.getInstance().getNewWebClient();
        Set<org.openqa.selenium.Cookie> cookiesDriver = driver.manage().getCookies();
        for (org.openqa.selenium.Cookie cookie : cookiesDriver) {
            Cookie cookieWebClient = new Cookie(domain, cookie.getName(), cookie.getValue());
            webClient.getCookieManager().addCookie(cookieWebClient);
        }

        String cookieJson = CommonUnit.transcookieToJson(webClient);
        System.out.println("cookieJson=============" + cookieJson);
        if (null != params) {
            Gson gson = new Gson();
            String param = gson.toJson(params);
            e_commerceTask.setParam(param);
        }
        //e_commerceTask.setCookies(cookieJson);
        e_commerceTask.setPhase(E_ComerceStatusCode.E_COMMERCE_LOGIN_SUCCESS_NEXTSTEP.getPhase());
        e_commerceTask.setPhase_status(E_ComerceStatusCode.E_COMMERCE_LOGIN_SUCCESS_NEXTSTEP.getPhasestatus());
        e_commerceTask.setDescription(E_ComerceStatusCode.E_COMMERCE_LOGIN_SUCCESS_NEXTSTEP.getDescription());
        System.out.println("taskBank=============" + e_commerceTask.toString());
        e_commerceTaskRepository.save(e_commerceTask);

    }

    /**
     * @param taskid
     * @return
     * @Des 判断是否正在爬取
     * @author zz
     */
    public boolean isDoing(String taskid) {

        E_CommerceTask e_commerceTask = e_commerceTaskRepository.findByTaskid(taskid);
        if (e_commerceTask.getPhase().equals("CRAWLER") && e_commerceTask.getPhase_status().equals("DOING")) {
            return true;
        }
        return false;
    }


    /**
     * 项目名称：common-microservice-e_commerce-service
     * 所属包名：app.service
     * 类描述：
     * 创建人：hyx
     * 创建时间：2017年12月26日
     *
     * @version 1
     * 返回值    void
     * @return 
     */
    public E_CommerceTask changecommerceTaskJDFinish(String taskid, WebDriver driver) {
        E_CommerceTask e_commerceTask = e_commerceTaskRepository.findByTaskid(taskid);

        if (null != e_commerceTask.getUserinfoStatus()
                && null != e_commerceTask.getAddressInfoStatus()
                && null != e_commerceTask.getBankCardInfoStatus()
                && null != e_commerceTask.getOrderInfoStatus()
                && null != e_commerceTask.getRenzhengInfoStatus()
                && null != e_commerceTask.getBtPrivilegeInfoStatus()) {
            e_commerceTask.setPhase(E_ComerceStatusCode.E_COMMERCE_CRAWLER_SUCCESS.getPhase());
            e_commerceTask.setPhase_status(E_ComerceStatusCode.E_COMMERCE_CRAWLER_SUCCESS.getPhasestatus());
            e_commerceTask.setDescription(E_ComerceStatusCode.E_COMMERCE_CRAWLER_SUCCESS.getDescription());
            e_commerceTask.setFinished(true);
            e_commerceTaskRepository.save(e_commerceTask);
//            driver.close();
        	agentService.releaseInstance(e_commerceTask.getCrawlerHost(), driver);

        }

        return e_commerceTask;
        
    }

    /**
     * @Des 判断是否爬取完成
     * @author wpy
     * @param taskid
     */
    public void changeFinish(String taskid) {
        E_CommerceTask e_commerceTask = e_commerceTaskRepository.findByTaskid(taskid);

        if (null != e_commerceTask.getUserinfoStatus()
                && null != e_commerceTask.getAddressInfoStatus()
                && null != e_commerceTask.getBankCardInfoStatus()
                && null != e_commerceTask.getOrderInfoStatus()
                && null != e_commerceTask.getRenzhengInfoStatus()
                && null != e_commerceTask.getBtPrivilegeInfoStatus()) {
            e_commerceTask.setPhase(E_ComerceStatusCode.E_COMMERCE_CRAWLER_SUCCESS.getPhase());
            e_commerceTask.setPhase_status(E_ComerceStatusCode.E_COMMERCE_CRAWLER_SUCCESS.getPhasestatus());
            e_commerceTask.setDescription(E_ComerceStatusCode.E_COMMERCE_CRAWLER_SUCCESS.getDescription());
            e_commerceTask.setFinished(true);
            e_commerceTaskRepository.save(e_commerceTask);
        }

    }

    public <T> void loingSucess(E_CommerceTask e_commerceTask) {

        e_commerceTask.setPhase(E_ComerceStatusCode.E_COMMERCE_LOGIN_SUCCESS_NEXTSTEP.getPhase());
        e_commerceTask.setPhase_status(E_ComerceStatusCode.E_COMMERCE_LOGIN_SUCCESS_NEXTSTEP.getPhasestatus());
        e_commerceTask.setDescription(E_ComerceStatusCode.E_COMMERCE_LOGIN_SUCCESS_NEXTSTEP.getDescription());
        System.out.println("e_commerceTask=============" + e_commerceTask.toString());
        e_commerceTaskRepository.save(e_commerceTask);

    }


    /**
     * @param finished
     * @param taskid
     * @return
     * @Des 改变task_bank状态
     */
    public E_CommerceTask systemClose(boolean finished, String taskid) {
        tracerLog.output("systemClose", taskid);
        E_CommerceTask task = e_commerceTaskRepository.findByTaskid(taskid);
        task.setError_code(TaskStatusCode.SYSTEM_QUIT.getError_code());
        task.setError_message(TaskStatusCode.SYSTEM_QUIT.getDescription());
        if (finished) {
            task.setFinished(finished);
        }
        task = e_commerceTaskRepository.save(task);
        return task;
    }



}
