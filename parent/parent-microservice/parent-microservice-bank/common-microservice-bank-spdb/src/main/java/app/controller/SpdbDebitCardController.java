package app.controller;

import app.commontracerlog.TracerLog;
import app.service.AgentService;
import app.service.SpdbDebitCardService;
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
 * @description: 浦发银行储蓄卡
 * @author: zmy
 * @date: 2017年11月9日
 */
@RestController
@Configuration
@RequestMapping("/bank/spdb/debitcard")
public class SpdbDebitCardController {
    @Autowired
    private TracerLog tracerLog;
    @Autowired
    private TaskBankStatusService taskBankStatusService;
    @Autowired
    private SpdbDebitCardService spdbDebitCardService;
    @Autowired
    private AgentService agentService;

    /**
     * @Des POST 登录的代理接口，对登录请求转发到限制的实例上
     * @param bankJsonBean
     */
    @PostMapping(path = "/loginAgent")
    public TaskBank loginAgent(@RequestBody BankJsonBean bankJsonBean) throws  Exception {
        tracerLog.output("SpdbDebitCardController.loginAgent", bankJsonBean.toString());
        tracerLog.qryKeyValue("taskid", bankJsonBean.getTaskid());
        TaskBank taskBank = taskBankStatusService.changeStatusLoginDoing(bankJsonBean);
        try {
            taskBank =  agentService.postAgent(bankJsonBean, "/bank/spdb/debitcard/login");
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
    public TaskBank spdbLogin(@RequestBody BankJsonBean bankJsonBean){
        tracerLog.output("SpdbDebitCardController.login", bankJsonBean.toString());
        tracerLog.qryKeyValue("taskid", bankJsonBean.getTaskid());
        //准备登陆
        TaskBank taskBank = taskBankStatusService.changeStatusLoginDoing(bankJsonBean);
        spdbDebitCardService.loginAndCrawler(bankJsonBean,taskBank);
        return taskBank;
    }


    @PostMapping(path = "/quit")
    public TaskBank quit(@RequestBody BankJsonBean bankJsonBean){
        tracerLog.qryKeyValue("taskid", bankJsonBean.getTaskid());
        tracerLog.qryKeyValue("quit", "调用公用释放资源方法，自动关闭task");
        TaskBank taskBank = spdbDebitCardService.quit(bankJsonBean);
        return taskBank;
    }
}
