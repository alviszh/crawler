package app.controller;

import app.commontracerlog.TracerLog;
import app.service.HousingFundBaoShanService;
import app.service.common.aop.ICrawlerLogin;
import com.crawler.housingfund.json.MessageLoginForHousing;
import com.crawler.mobile.json.ResultData;
import com.crawler.mobile.json.StatusCodeEnum;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Configuration
@RequestMapping("/housing/baoshan")
public class HousingFundBaoShanController extends HousingBasicController{

    @Autowired
    private TracerLog tracer;
    @Autowired
    private ICrawlerLogin iCrawlerLogin;


    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResultData<TaskHousing> login(@RequestBody MessageLoginForHousing messageLoginForHousing) {
        tracer.qryKeyValue("action.login.taskid", messageLoginForHousing.getTask_id());
        tracer.addTag("action.login.auth", "登录号码是：====>"+messageLoginForHousing.getNum()+"密码是：====>"+messageLoginForHousing.getPassword());
        TaskHousing taskHousing =findTaskHousing(messageLoginForHousing.getTask_id());
        ResultData<TaskHousing> result = new ResultData<TaskHousing>();

        iCrawlerLogin.login(messageLoginForHousing);
        result.setData(taskHousing);
        return result;
    }

    @RequestMapping(value = "/crawler", method = RequestMethod.POST)
    public ResultData<TaskHousing> crawler(@RequestBody MessageLoginForHousing messageLoginForHousing) throws Exception {
        tracer.qryKeyValue("action.crawler.taskid",messageLoginForHousing.getTask_id());
        TaskHousing taskHousing =findTaskHousing(messageLoginForHousing.getTask_id());
        ResultData<TaskHousing> result = new ResultData<TaskHousing>();

        result.setData(taskHousing);
        Thread.sleep(1000);
        //爬取所有数据
        iCrawlerLogin.getAllData(messageLoginForHousing);
        return result;
    }
}
