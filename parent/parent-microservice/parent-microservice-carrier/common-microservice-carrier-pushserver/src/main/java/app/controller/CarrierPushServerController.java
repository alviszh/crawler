package app.controller;


import app.commontracerlog.TracerLog;
import app.service.MobileSendService;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;
import com.microservice.dao.repository.crawler.mobile.TaskMobileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public void testApi(){
        System.out.println("================  test api  ============== ");
        tracerLog.addTag("testApi","回调方法");
    }
}
