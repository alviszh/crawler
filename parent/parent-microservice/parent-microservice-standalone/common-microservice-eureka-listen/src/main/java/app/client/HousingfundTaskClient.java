package app.client;

import org.springframework.cloud.netflix.eureka.server.event.EurekaInstanceCanceledEvent;
import org.springframework.cloud.netflix.eureka.server.event.EurekaInstanceRegisteredEvent;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.crawler.eureka.listen.EurekaListenbean;



@FeignClient("HousingFund-Task")
public interface HousingfundTaskClient {

	/**
     * 服务器注册时间
     * @param instanceInfo
     * @return
     */
    @PostMapping(path = "/housingfund/eurekalisten")
    public void eurekalisten(@RequestBody EurekaListenbean eurekaListenbean);
   /* 
    *//**
     * 服务器断线事件
     * @param instanceInfo
     * @return
     *//*
    @PostMapping(path = "/housingfund/eurekalisten/canceled")
    public void canceled(@RequestBody EurekaListenbean eurekaListenbean);*/
}
