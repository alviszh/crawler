package app.controller;


import app.commontracerlog.TracerLog;
import app.service.MobileSendService;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;
import com.microservice.dao.repository.crawler.mobile.TaskMobileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/carrier/pushserver")
public class CarrierPushServerController {

    @Autowired
    private MobileSendService mobileSendService;

    @Autowired
    private TaskMobileRepository taskMobileRepository;
    @Autowired
    private TracerLog tracerLog;


    //测试接口
    @GetMapping("/sendMessageResult")
    public void sendMessageResult(){
        TaskMobile taskMobile = taskMobileRepository.findByTaskid("c59fac2f-fa02-47d8-9bac-4d428de95a7d");
        System.out.println("taskmobile=" + taskMobile);

        mobileSendService.sendMessageResult(taskMobile);
    }

    //测试回调接口
    @PostMapping("/testApi")
    public String testApi(@RequestBody String messageResult){
        System.out.println("================  test api  ============== " + messageResult);
        tracerLog.addTag("testApi","回调方法");
        return "test success";
    }
}
