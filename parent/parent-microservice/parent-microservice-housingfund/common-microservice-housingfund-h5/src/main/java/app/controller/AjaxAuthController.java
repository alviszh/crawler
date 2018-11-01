package app.controller;

import com.crawler.housingfund.json.AreaCode;
import com.crawler.housingfund.json.TaskHousingfund;
import app.client.fund.HousingfundTaskClient;
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
@RequestMapping("/h5/fund")
public class AjaxAuthController {
    private static final Logger log= LoggerFactory.getLogger(AjaxAuthController.class);

    @Autowired
    private HousingfundTaskClient housingfundTaskClient;

    /*城市列表*/
    @RequestMapping(value = "/cityList", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody
    List<AreaCode> cityList() {
        log.info("-----------获取城市列表------------");
        List<AreaCode> areaCodes = housingfundTaskClient.getCitys();
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
    TaskHousingfund intervalStatus(@RequestParam(name = "taskId") String taskId) {
        log.info("-----------获取task的状态------------" + taskId);
        TaskHousingfund taskHousingfunda = housingfundTaskClient.taskStatus(taskId);
        log.info("-----------taskHousingfunda------------" + taskHousingfunda);
        return  taskHousingfunda;
    }
}
