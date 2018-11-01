package app.controller;

import app.commontracerlog.TracerLog;
import app.service.AgentService;
import app.service.BeijingBankDebitCardService;
import app.service.TaskBankStatusService;
import com.crawler.bank.json.BankJsonBean;
import com.crawler.bank.json.BankStatusCode;
import com.microservice.dao.entity.crawler.bank.basic.TaskBank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 北京银行-储蓄卡
 * Created by zmy on 2018/3/9.
 */
@RestController
@Configuration
@RequestMapping("/bank/beijing/debitcard")
public class BeijingBankDebitCardController {

    @Autowired
    private TracerLog tracerLog;
    @Autowired
    private TaskBankStatusService taskBankStatusService;
    @Autowired
    private AgentService agentService;
    @Autowired
    private BeijingBankDebitCardService beijingBankDebitCardService;

    /**
     * @Des POST 登录的代理接口，对登录请求转发到限制的实例上
     * @param bankJsonBean
     */
    @PostMapping(path = "/loginAgent")
    public TaskBank loginAgent(@RequestBody BankJsonBean bankJsonBean) throws  Exception {
        tracerLog.addTag("BeijingBankDebitCardController.loginAgent", bankJsonBean.toString());
        tracerLog.addTag("taskid", bankJsonBean.getTaskid());
        TaskBank taskBank = taskBankStatusService.changeStatusLoginDoing(bankJsonBean);
        try {
            taskBank = agentService.postAgent(bankJsonBean, "/bank/beijing/debitcard/login", 3 * 60 * 1000L);
        } catch (RuntimeException e) {
            taskBank = taskBankStatusService.changeStatus(BankStatusCode.BANK_AGENT_ERROR.getPhase(),
                    BankStatusCode.BANK_AGENT_ERROR.getPhasestatus(),
                    BankStatusCode.BANK_AGENT_ERROR.getDescription(),
                    BankStatusCode.BANK_AGENT_ERROR.getError_code(), true, bankJsonBean.getTaskid());
            tracerLog.addTag("BeijingBankDebitCardController.loginAgent.exception", e.getMessage());
            System.out.println("BeijingBankDebitCardController.loginAgent.exception=" + e.getMessage());
            return taskBank;
        }
        return taskBank;
    }

    /**
     * @des: 登录
     * @param bankJsonBean
     * @return
     */
    @PostMapping(path = "/login")
    public TaskBank login(@RequestBody BankJsonBean bankJsonBean) throws Exception{
        tracerLog.addTag("BeijingBankDebitCardController.login", bankJsonBean.toString());
        tracerLog.addTag("taskid", bankJsonBean.getTaskid());
        //准备登陆
//        TaskBank taskBank = taskBankStatusService.changeStatusLoginDoing(bankJsonBean);
        TaskBank taskBank = beijingBankDebitCardService.loginAndCrawler(bankJsonBean);
        return taskBank;
    }

    @PostMapping(path = "/quit")
    public TaskBank quit(@RequestBody BankJsonBean bankJsonBean){
        tracerLog.addTag("taskid", bankJsonBean.getTaskid());
        tracerLog.addTag("quit", "调用公用释放资源方法，自动关闭task");
        TaskBank taskBank = beijingBankDebitCardService.quit(bankJsonBean);
        return taskBank;
    }
}
