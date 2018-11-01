package app.controller;

import app.service.HousingFundSZAnhuiGetAllDataService;
import app.service.HousingFundSZAnhuiService;
import app.service.common.aop.ICrawlerLogin;
import com.crawler.housingfund.json.MessageLoginForHousing;
import com.crawler.mobile.json.ResultData;
import com.crawler.mobile.json.StatusCodeEnum;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Configuration
@RequestMapping("/housing/SZAnhui")
public class HousingFundSZAnhuiController extends HousingBasicController {
    @Autowired
    private ICrawlerLogin iCrawlerLogin;
    @Autowired
    private HousingFundSZAnhuiGetAllDataService housingFundSZAnhuiGetAllDataService;

    @PostMapping(value="/login")
    public ResultData<TaskHousing> login(@RequestBody MessageLoginForHousing messageLoginForHousing){
        tracer.qryKeyValue("taskid", messageLoginForHousing.getTask_id());

        TaskHousing taskHousing = taskHousingRepository.findByTaskid(messageLoginForHousing.getTask_id());
        ResultData<TaskHousing> result = new ResultData<TaskHousing>();
        try {
            iCrawlerLogin.login(messageLoginForHousing);
        } catch (Exception e) {
            e.printStackTrace();
        }
        result.setData(taskHousing);
        return result;
    }

    @PostMapping(value="/crawler")
    public ResultData<TaskHousing> crawler(@RequestBody MessageLoginForHousing messageLoginForHousing) throws Exception{
        tracer.qryKeyValue("taskid", messageLoginForHousing.getTask_id());
        ResultData<TaskHousing> result = new ResultData<TaskHousing>();
        try {
            TaskHousing taskHousing = iCrawlerLogin.getAllData(messageLoginForHousing);
            result.setData(taskHousing);
        } catch (Exception e) {
            tracer.addTag("parser.crawler.taskid.e", e.toString());
            e.printStackTrace();
        }
        return result;
    }
}
