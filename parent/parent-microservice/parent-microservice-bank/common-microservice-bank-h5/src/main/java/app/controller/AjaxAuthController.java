package app.controller;

import com.crawler.bank.json.BankCode;
import com.crawler.bank.json.TaskBank;
import app.client.bank.BankTaskClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/h5/bank")
public class AjaxAuthController {
    private static final Logger log= LoggerFactory.getLogger(AjaxAuthController.class);

    @Autowired
    private BankTaskClient bankTaskClient;

    /*卡列表*/
    @RequestMapping(value = "/getBank", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody
    List<BankCode> getBank() {
        log.info("-----------获取卡列表------------");
        List<BankCode> bankCodes = bankTaskClient.getBank();
        /*List<AreaCode> areaCodes = new ArrayList<AreaCode>();
        areaCodes.add(new AreaCode());*/
//        log.info("-----------areaCodes------------" + areaCodes);
        return  bankCodes;
    }


    /**
     * 间隔一秒获取task的状态
     * @param taskid
     * @return
     */
    @RequestMapping(value = "/tasks/status", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody
    TaskBank intervalStatus(@RequestParam(name = "taskid") String taskid) {
        log.info("-----------获取task的状态------------" + taskid);
        TaskBank taskBanka = bankTaskClient.taskStatus(taskid);
        log.info("-----------taskHousingfunda------------" + taskBanka);
        return  taskBanka;
    }
}
