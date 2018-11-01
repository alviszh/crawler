package app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crawler.e_commerce.json.E_ComerceStatusCode;
import com.crawler.e_commerce.json.E_CommerceJsonBean;
import com.microservice.dao.entity.crawler.e_commerce.basic.E_CommerceTask;
import com.microservice.dao.repository.crawler.e_commerce.basic.E_CommerceTaskRepository;

import app.client.MiddleClient;
import app.commontracerlog.TracerLog;
import app.service.AgentService;
import app.service.E_CommerceTaskStatusService;
import app.service.TaobaoCommonService;
import app.service.TaobaoService;

@RestController
@Configuration
@RequestMapping("/e_commerce/taobao")
public class TaobaoController {
//    @Autowired
//    private Tracer tracer;

    @Autowired
    private TracerLog tracerLog;
    @Autowired
    private E_CommerceTaskStatusService e_commerceTaskStatusService;

//    @Autowired
//    private RestTemplate restTemplate;

    @Autowired
    private TaobaoService taobaoService;

    @Autowired
    private MiddleClient middleClient;

    @Value("${spring.application.name}")
    String appName;

    @Autowired
    private TaobaoCommonService taobaoCommonService;
    
    @Autowired
    private AgentService agentService;
    
    @Autowired
    private E_CommerceTaskRepository e_commerceTaskRepository;


//    @PostMapping(path = "/loginAgent")
//    public E_CommerceTask loginAgent(@RequestBody E_CommerceJsonBean e_commerceJsonBean) {
//        tracer.addTag("username", e_commerceJsonBean.getUsername());
//        tracer.addTag("taskid", e_commerceJsonBean.getTaskid());
//        tracer.addTag("e_commerceJsonBean", e_commerceJsonBean.toString());
//        E_CommerceTask task = agentService.postAgent(e_commerceJsonBean, "/e_commerce/taobao/login", 10 * 60000L);
//        e_commerceJsonBean.setIp(task.getCrawlerHost());
//        e_commerceJsonBean.setPort(task.getCrawlerPort());
//        tracer.addTag("host", task.getCrawlerHost());
//        // e_commerceJsonBean.setWebdriverHandle(task.getWebdriverHandle());
//        return task;
//    }
//
//    @PostMapping(path = "/login")
//    public E_CommerceTask login(@RequestBody E_CommerceJsonBean e_commerceJsonBean) {
//
//        tracer.addTag("crawler.taobao.login.start", e_commerceJsonBean.getTaskid());
//        String loginType = e_commerceJsonBean.getLogintype();
//        E_CommerceTask task = null;
//        if (loginType.equals("alipay_name")) {
//            task = login4Alipay(e_commerceJsonBean);
//        } else if (loginType.equals("tb_qr")) {
//            task = getTaobaoQRcode(e_commerceJsonBean);
//        } else {
//            task = e_commerceTaskStatusService.changeStatus(TaskStatusCode.PARAMETER_ERROR.getPhase(),
//                    TaskStatusCode.PARAMETER_ERROR.getPhasestatus(), TaskStatusCode.PARAMETER_ERROR.getDescription(),
//                    TaskStatusCode.PARAMETER_ERROR.getError_code(), false, e_commerceJsonBean.getTaskid());
//        }
//        e_commerceJsonBean.setIp(task.getCrawlerHost());
//        e_commerceJsonBean.setPort(task.getCrawlerPort());
//        return task;
//    }

    @PostMapping(path = "/alipay/loginAgent", consumes = "application/json")
    public E_CommerceTask login4AlipayAgent(@RequestBody E_CommerceJsonBean e_commerceJsonBean) {
    	tracerLog.addTag("crawler.taobao.login4Alipay", e_commerceJsonBean.getTaskid());
        E_CommerceTask task = e_commerceTaskRepository.findByTaskid(e_commerceJsonBean.getTaskid());
        if(null != task.getCrawlerHost() && !task.getCrawlerHost().contains("")){
        	tracerLog.addTag("crawler.taobao.login4AlipayNum", "postAgentCombo");
        	e_commerceJsonBean.setIp(task.getCrawlerHost());
            e_commerceJsonBean.setPort(task.getCrawlerPort());
            task = agentService.postAgentCombo(e_commerceJsonBean, "/e_commerce/taobao/alipay/login");
        }else{
        	tracerLog.addTag("crawler.taobao.login4AlipayNum", "postAgent");
        	task = agentService.postAgent(e_commerceJsonBean, "/e_commerce/taobao/alipay/login", 10 * 60000L);
            e_commerceJsonBean.setIp(task.getCrawlerHost());
            e_commerceJsonBean.setPort(task.getCrawlerPort());
            e_commerceTaskRepository.save(task);
        }
        return task;
    }

    @PostMapping(path = "/alipay/login")
    public E_CommerceTask login4Alipay(@RequestBody E_CommerceJsonBean e_commerceJsonBean) {

        tracerLog.output("crawler.taobao.login4Alipay", e_commerceJsonBean.getTaskid());

        //正则验证是否为 手机号 或者 邮箱
        /*if (e_commerceJsonBean.getUsername().matches("^[a-z0-9]+([._\\\\-]*[a-z0-9])*@([a-z0-9]+[-a-z0-9]*[a-z0-9]+.){1,63}[a-z0-9]+$")
                || e_commerceJsonBean.getUsername().matches("^1\\d{10}$")) {
            E_CommerceTask e_commerceTask = e_commerceTaskStatusService.changeStatus(E_ComerceStatusCode.E_COMMERCE_LOGIN_DOING.getPhase(),
            		E_ComerceStatusCode.E_COMMERCE_LOGIN_DOING.getPhasestatus(), E_ComerceStatusCode.E_COMMERCE_LOGIN_DOING.getDescription(),
            		E_ComerceStatusCode.E_COMMERCE_LOGIN_DOING.getError_code(), false, e_commerceJsonBean.getTaskid());
            e_commerceTask = taobaoCommonService.login(e_commerceJsonBean);
            return e_commerceTask;
        } else {
            return e_commerceTaskStatusService.changeStatus(E_ComerceStatusCode.E_COMMERCE_LOGIN_LOGINNAME_NOT_EMAIL_OR_PHONENO.getPhase(),
            		E_ComerceStatusCode.E_COMMERCE_LOGIN_LOGINNAME_NOT_EMAIL_OR_PHONENO.getPhasestatus(), E_ComerceStatusCode.E_COMMERCE_LOGIN_LOGINNAME_NOT_EMAIL_OR_PHONENO.getDescription(),
            		E_ComerceStatusCode.E_COMMERCE_LOGIN_LOGINNAME_NOT_EMAIL_OR_PHONENO.getError_code(), false, e_commerceJsonBean.getTaskid());
        }*/
        E_CommerceTask e_commerceTask = e_commerceTaskStatusService.changeStatus(E_ComerceStatusCode.E_COMMERCE_LOGIN_DOING.getPhase(),
        		E_ComerceStatusCode.E_COMMERCE_LOGIN_DOING.getPhasestatus(), E_ComerceStatusCode.E_COMMERCE_LOGIN_DOING.getDescription(),
        		E_ComerceStatusCode.E_COMMERCE_LOGIN_DOING.getError_code(), false, e_commerceJsonBean.getTaskid());
        e_commerceTask = taobaoCommonService.login(e_commerceJsonBean);
        return e_commerceTask;
    }

    /**
     * 获取淘宝登录二维码代理
     *
     * @param e_commerceJsonBean
     * @return
     */
    @PostMapping(path = "/base64ImageAgent", consumes = "application/json")
    public E_CommerceTask getTaobaoQRcodeAgent(@RequestBody E_CommerceJsonBean e_commerceJsonBean) {
    	tracerLog.addTag("crawler.taobao.getTaobaoQRcodeAgent", e_commerceJsonBean.getTaskid());
        E_CommerceTask task = new E_CommerceTask();
		try {
			task = agentService.postAgent(e_commerceJsonBean, "/e_commerce/taobao/base64Image", 10 * 60000L);
//			e_commerceJsonBean.setIp(task.getCrawlerHost());
//	        e_commerceJsonBean.setPort(task.getCrawlerPort());
	        e_commerceTaskRepository.save(task);
		} catch (Exception e) {
			tracerLog.output("crawler.taobao.loginAgent.exception", e.getMessage());
			task = e_commerceTaskStatusService.changeStatus(E_ComerceStatusCode.E_COMMERCE_AGENT_ERROR.getPhase(),
					E_ComerceStatusCode.E_COMMERCE_AGENT_ERROR.getPhasestatus(),
					E_ComerceStatusCode.E_COMMERCE_AGENT_ERROR.getDescription(),
					E_ComerceStatusCode.E_COMMERCE_AGENT_ERROR.getError_code(), true, e_commerceJsonBean.getTaskid());
		}
        // e_commerceJsonBean.setWebdriverHandle(task.getWebdriverHandle());
        return task;
    }

    /**
     * 获取淘宝登录二维码
     *
     * @param e_commerceJsonBean
     * @return
     */
    @PostMapping(path = "/base64Image")
    public E_CommerceTask getTaobaoQRcode(@RequestBody E_CommerceJsonBean e_commerceJsonBean) {

        tracerLog.output("crawler.taobao.getTaobaoQRcode", e_commerceJsonBean.getTaskid());

        E_CommerceTask e_commerceTask = e_commerceTaskStatusService.changeStatusLoginDoing(e_commerceJsonBean);
        e_commerceJsonBean.setLogintype("taobaoQRcode");
        tracerLog.output("crawler.taobao.getTaobaoQRcode.e_commerceJsonBean", e_commerceJsonBean.toString());
        e_commerceTask = taobaoCommonService.getQRcode(e_commerceJsonBean);
        return e_commerceTask;
    }

    
    /**
     * 获取支付宝登录二维码代理
     *
     * @param e_commerceJsonBean
     * @return
     * @author wpy
     */
    @PostMapping(path = "/alipay/base64ImageAgent", consumes = "application/json")
    public E_CommerceTask getAliQRcodeAgent(@RequestBody E_CommerceJsonBean e_commerceJsonBean) {
    	tracerLog.addTag("crawler.taobao.getAliQRcodeAgent", e_commerceJsonBean.getTaskid());
        E_CommerceTask task = new E_CommerceTask();
		try {
			task = agentService.postAgent(e_commerceJsonBean, "/e_commerce/taobao/alipay/base64Image", 5 * 60000L);
			e_commerceJsonBean.setIp(task.getCrawlerHost());
			e_commerceJsonBean.setPort(task.getCrawlerPort());
			e_commerceTaskRepository.save(task);
		} catch (Exception e) {
			tracerLog.output("crawler.taobao.loginAgent.exception", e.getMessage());
			task = e_commerceTaskStatusService.changeStatus(E_ComerceStatusCode.E_COMMERCE_AGENT_ERROR.getPhase(),
					E_ComerceStatusCode.E_COMMERCE_AGENT_ERROR.getPhasestatus(),
					E_ComerceStatusCode.E_COMMERCE_AGENT_ERROR.getDescription(),
					E_ComerceStatusCode.E_COMMERCE_AGENT_ERROR.getError_code(), true, e_commerceJsonBean.getTaskid());
		}
        // e_commerceJsonBean.setWebdriverHandle(task.getWebdriverHandle());
        return task;
    }

    /**
     * 获取支付宝登录二维码
     *
     * @param e_commerceJsonBean
     * @return
     * @author wpy
     */
    @PostMapping(path = "/alipay/base64Image")
    public E_CommerceTask getAliQRcode(@RequestBody E_CommerceJsonBean e_commerceJsonBean) {

        tracerLog.output("crawler.taobao.getAliQRcode", e_commerceJsonBean.getTaskid());
        E_CommerceTask e_commerceTask = e_commerceTaskStatusService.changeStatusLoginDoing(e_commerceJsonBean);
        e_commerceJsonBean.setLogintype("aliQRcode");
        e_commerceTask = taobaoCommonService.getQRcode(e_commerceJsonBean);
        return e_commerceTask;
    }
    
    
    

    /**
     * 验证支付宝二维码是否扫码登录代理
     *
     * @param e_commerceJsonBean
     * @return
     * @author wpy
     */
    @PostMapping(path = "/alipay/checkQRcodeAgent")
    public E_CommerceTask checkAliQRcodeAgent(@RequestBody E_CommerceJsonBean e_commerceJsonBean) {
    	tracerLog.addTag("crawler.taobao.checkAliQRcodeAgent.taskid", e_commerceJsonBean.getTaskid());

        System.out.println("crawler.taobao.checkAliQRcodeAgent +++++" + e_commerceJsonBean.getTaskid());
        E_CommerceTask e_commerceTask = e_commerceTaskRepository.findByTaskid(e_commerceJsonBean.getTaskid());
        e_commerceJsonBean.setIp(e_commerceTask.getCrawlerHost());
        e_commerceJsonBean.setPort(e_commerceTask.getCrawlerPort());
        e_commerceJsonBean.setWebdriverHandle(e_commerceTask.getWebdriverHandle());
        E_CommerceTask task = agentService.postAgentCombo(e_commerceJsonBean, "/e_commerce/taobao/alipay/checkQRcode");
        return task;
    }

    /**
     * 验证支付宝二维码是否扫码登录
     *
     * @param e_commerceJsonBean
     * @return
     * @author wpy
     */
    @PostMapping(path = "/alipay/checkQRcode")
    public E_CommerceTask checkAliQRcode(@RequestBody E_CommerceJsonBean e_commerceJsonBean) {
        tracerLog.output("crawler.taobao.checkAliQRcode", e_commerceJsonBean.getTaskid());
        System.out.println("crawler.taobao.checkAliQRcode +++++" + e_commerceJsonBean.getTaskid());
        e_commerceJsonBean.setLogintype("aliQRcode");
        E_CommerceTask e_commerceTask = taobaoCommonService.checkQRcode(e_commerceJsonBean);
        return e_commerceTask;
    }

    
    /**
     * 验证淘宝二维码是否扫码代理
     *
     * @param e_commerceJsonBean
     * @return
     */
    @PostMapping(path = "/checkQRcodeAgent")
    public E_CommerceTask checkQRcodeAgent(@RequestBody E_CommerceJsonBean e_commerceJsonBean) {
    	tracerLog.addTag("crawler.taobao.login", e_commerceJsonBean.getTaskid());

        System.out.println("crawler.taobao.login4QRcode +++++" + e_commerceJsonBean.getTaskid());
        E_CommerceTask e_commerceTask = e_commerceTaskRepository.findByTaskid(e_commerceJsonBean.getTaskid());
        e_commerceJsonBean.setIp(e_commerceTask.getCrawlerHost());
        e_commerceJsonBean.setPort(e_commerceTask.getCrawlerPort());
        e_commerceJsonBean.setWebdriverHandle(e_commerceTask.getWebdriverHandle());
        E_CommerceTask task = agentService.postAgentCombo(e_commerceJsonBean, "/e_commerce/taobao/checkQRcode");
        return task;
    }

    /**
     * 验证淘宝二维码是否扫码
     *
     * @param e_commerceJsonBean
     * @return
     */
    @PostMapping(path = "/checkQRcode")
    public E_CommerceTask checkQRcode(@RequestBody E_CommerceJsonBean e_commerceJsonBean) {

        tracerLog.output("crawler.taobao.login4QRcode", e_commerceJsonBean.getTaskid());
        System.out.println("crawler.taobao.login4QRcode +++++" + e_commerceJsonBean.getTaskid());
        e_commerceJsonBean.setLogintype("aliQRcode");
        E_CommerceTask e_commerceTask = taobaoCommonService.checkQRcode(e_commerceJsonBean);
        return e_commerceTask;
    }
    

    @PostMapping(path = "/verfiySMSAgent")
    public E_CommerceTask verfiySMSAgent(@RequestBody E_CommerceJsonBean e_commerceJsonBean) throws Exception {
        tracerLog.output("crawler.taobao.smsverfiy.agent", e_commerceJsonBean.toString());
        E_CommerceTask e_commerceTask = e_commerceTaskStatusService.changeStatus(E_ComerceStatusCode.E_COMMERCE_VALIDATE_CODE_DONING.getPhase(),
        		E_ComerceStatusCode.E_COMMERCE_VALIDATE_CODE_DONING.getPhasestatus(),
                E_ComerceStatusCode.E_COMMERCE_VALIDATE_CODE_DONING.getDescription(),
                null, false, e_commerceJsonBean.getTaskid());

        e_commerceJsonBean.setIp(e_commerceTask.getCrawlerHost());
        e_commerceJsonBean.setPort(e_commerceTask.getCrawlerPort());
        e_commerceJsonBean.setWebdriverHandle(e_commerceTask.getWebdriverHandle());

        // 验证短信(同步)
        e_commerceTask = agentService.postAgentCombo(e_commerceJsonBean, "/e_commerce/taobao/verfiySMS");
        return e_commerceTask;
    }

    @PostMapping(path = "/verfiySMS")
    public E_CommerceTask verfiySMS(@RequestBody E_CommerceJsonBean bankJsonBean) throws Exception {
        tracerLog.output("crawler.bank.smsverfiy", bankJsonBean.toString());
        E_CommerceTask e_commerceTask = taobaoService.verfiySMS(bankJsonBean);
        return e_commerceTask;
    }


    @PostMapping(path = "/crawlerAgent")
    public E_CommerceTask crawlerAgent(@RequestBody E_CommerceJsonBean e_commerceJsonBean) throws Exception {
        tracerLog.output("crawler.bank.crawler.agent", e_commerceJsonBean.getTaskid());
        E_CommerceTask e_commerceTask = e_commerceTaskStatusService.changeStatus(E_ComerceStatusCode.E_COMMERCE_LOGIN_SUCCESS_NEXTSTEP.getPhase(),
        		E_ComerceStatusCode.E_COMMERCE_LOGIN_SUCCESS_NEXTSTEP.getPhasestatus(),
                E_ComerceStatusCode.E_COMMERCE_LOGIN_SUCCESS_NEXTSTEP.getDescription(),
                null, false, e_commerceJsonBean.getTaskid());
        ;

        e_commerceJsonBean.setIp(e_commerceTask.getCrawlerHost());
        e_commerceJsonBean.setPort(e_commerceTask.getCrawlerPort());
        e_commerceJsonBean.setWebdriverHandle(e_commerceTask.getWebdriverHandle());

        e_commerceTask = agentService.postAgentCombo(e_commerceJsonBean, "/e_commerce/taobao/crawler");
        return e_commerceTask;

    }

    @PostMapping(path = "/crawler")
    public E_CommerceTask crawl(@RequestBody E_CommerceJsonBean e_commerceJsonBean) {
        tracerLog.output("crawler.taobao.crawler", e_commerceJsonBean.getTaskid());
        E_CommerceTask e_commerceTask = taobaoCommonService.getAllData(e_commerceJsonBean);
        return e_commerceTask;
    }

    @PostMapping(path = "/quit")
    public E_CommerceTask quit(@RequestBody E_CommerceJsonBean bankJsonBean) {
        return taobaoService.quit(bankJsonBean);
    }

    @PostMapping(path = "/alipay/quit")
    public E_CommerceTask alipayQuit(@RequestBody E_CommerceJsonBean bankJsonBean) {
        return taobaoService.quit(bankJsonBean);
    }
}
