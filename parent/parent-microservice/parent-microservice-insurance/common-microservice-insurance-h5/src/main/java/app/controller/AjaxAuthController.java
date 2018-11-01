package app.controller;

import com.crawler.insurance.json.AreaCode;
import com.crawler.insurance.json.TaskInsurance;
import app.client.insur.InsuranceTaskClient;
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
@RequestMapping("/h5/insur")
public class AjaxAuthController {
    private static final Logger log= LoggerFactory.getLogger(AjaxAuthController.class);

    @Autowired
    private InsuranceTaskClient insuranceTaskClient;

    /*城市列表*/
    @RequestMapping(value = "/cityList", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody
    List<AreaCode> cityList() {
        log.info("-----------获取城市列表------------");
        List<AreaCode> areaCodes = insuranceTaskClient.getCitys();
        /*List<AreaCode> areaCodes = new ArrayList<AreaCode>();
        areaCodes.add(new AreaCode());*/
//        log.info("-----------areaCodes------------" + areaCodes);
        return  areaCodes;
    }

    /**
     * 间隔一秒获取task的状态
     * @param taskId
     * @return
     */
    @RequestMapping(value = "/tasks/status", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody
    TaskInsurance intervalStatus(@RequestParam(name = "taskId") String taskId) {
        log.info("-----------获取task的状态------------" + taskId);
        TaskInsurance taskInsurancea = insuranceTaskClient.taskStatus(taskId);
        log.info("-----------taskInsurancea------------" + taskInsurancea);
        return  taskInsurancea;
    }
}
