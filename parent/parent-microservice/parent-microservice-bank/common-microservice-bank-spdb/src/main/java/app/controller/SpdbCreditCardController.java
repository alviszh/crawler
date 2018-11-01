package app.controller;

import app.commontracerlog.TracerLog;
import app.service.AgentService;
import app.service.SpdbCreditCardService;
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
import org.xvolks.jnative.exceptions.NativeException;

/**
 * @description: 浦发银行信用卡
 * @author: zmy
 * @date: 2017年12月5日
 */
@RestController
@Configuration
@RequestMapping("/bank/spdb/creditcard")
public class SpdbCreditCardController {
    @Autowired
    private TracerLog tracerLog;
    @Autowired
    private TaskBankStatusService taskBankStatusService;
    @Autowired
    private AgentService agentService;
    @Autowired
    private SpdbCreditCardService spdbCreditCardService;

    /**
     * @Des POST 登录的代理接口，对登录请求转发到限制的实例上
     * @param bankJsonBean
     */
    @PostMapping(path = "/loginAgent")
    public TaskBank loginAgent(@RequestBody BankJsonBean bankJsonBean) throws  Exception {
        tracerLog.output("SpdbCreditCardController.loginAgent", bankJsonBean.toString());
        tracerLog.output("taskid", bankJsonBean.getTaskid());
        TaskBank taskBank = taskBankStatusService.changeStatusLoginDoing(bankJsonBean);
        try {
            agentService.postAgent(bankJsonBean, "/bank/spdb/creditcard/login", 10 * 60 * 1000L);
        }catch (RuntimeException e) {
            taskBank = taskBankStatusService.changeStatus(BankStatusCode.BANK_AGENT_ERROR.getPhase(),
                    BankStatusCode.BANK_AGENT_ERROR.getPhasestatus(),
                    BankStatusCode.BANK_AGENT_ERROR.getDescription(),
                    BankStatusCode.BANK_AGENT_ERROR.getError_code(),true,bankJsonBean.getTaskid());
            tracerLog.output("SpdbDebitCardController.loginAgent.exception", e.getMessage());
            System.out.println("SpdbDebitCardController.loginAgent.exception="+ e.getMessage());
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
    public TaskBank spdbLogin(@RequestBody BankJsonBean bankJsonBean) throws Exception{
        tracerLog.output("SpdbDebitCardController.login", bankJsonBean.toString());
        tracerLog.output("taskid", bankJsonBean.getTaskid());
        //准备登陆
        TaskBank taskBank = taskBankStatusService.changeStatusLoginDoing(bankJsonBean);
        spdbCreditCardService.login(bankJsonBean,taskBank);
        return taskBank;
    }

    @PostMapping(path = "/verfiySMSAgent")
    public TaskBank verfiySMSAgent(@RequestBody BankJsonBean bankJsonBean) throws Exception {
        tracerLog.output("SpdbCreditCardController.verfiySMSAgent", bankJsonBean.toString());
        tracerLog.output("taskid", bankJsonBean.getTaskid());
        TaskBank taskBank = taskBankStatusService.changeStatus(BankStatusCode.BANK_VALIDATE_CODE_DONING.getPhase(),
                BankStatusCode.BANK_VALIDATE_CODE_DONING.getPhasestatus(),
                BankStatusCode.BANK_VALIDATE_CODE_DONING.getDescription(),
                null, false, bankJsonBean.getTaskid());

        bankJsonBean.setIp(taskBank.getCrawlerHost());
        bankJsonBean.setPort(taskBank.getCrawlerPort());
        bankJsonBean.setWebdriverHandle(taskBank.getWebdriverHandle());

        // 验证短信(同步)
        taskBank =  agentService.postAgentCombo(bankJsonBean, "/bank/spdb/creditcard/verfiySMS");
        return taskBank;
    }

    @PostMapping(path = "/verfiySMS")
    public TaskBank verfiySMS(@RequestBody BankJsonBean bankJsonBean)
            throws IllegalAccessException, NativeException, Exception {
        tracerLog.output("SpdbCreditCardController.verfiySMS", bankJsonBean.toString());
        tracerLog.output("taskid", bankJsonBean.getTaskid());
        TaskBank taskBank = taskBankStatusService.changeStatus(BankStatusCode.BANK_VALIDATE_CODE_DONING.getPhase(),
                BankStatusCode.BANK_VALIDATE_CODE_DONING.getPhasestatus(),
                BankStatusCode.BANK_VALIDATE_CODE_DONING.getDescription(),
                null, false, bankJsonBean.getTaskid());
        bankJsonBean.setWebdriverHandle(taskBank.getWebdriverHandle());
        spdbCreditCardService.verfiySMSAndCrawler(bankJsonBean);
//        TaskBank taskBank = spdbCreditCardService.verfiySMS(bankJsonBean);
        return taskBank;
    }

    @PostMapping(path = "/quit")
    public TaskBank quit(@RequestBody BankJsonBean bankJsonBean){
        tracerLog.output("taskid", bankJsonBean.getTaskid());
        tracerLog.output("quit", "调用公用释放资源方法，自动关闭task");
        TaskBank taskBank = spdbCreditCardService.quit(bankJsonBean);
        return taskBank;
    }
}
