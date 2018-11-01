package app.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import app.entity.mobileETL.WebData;

/**
 *
 */
//@FeignClient("mobile-etl")
@FeignClient(name = "mobileETL", url = "${api.mobileETL.url}")
public interface MobileETLClient {


    @GetMapping("/etl/carrier/tasks/mobileinfo/detail")
    WebData mobileInfo(@RequestParam("taskid") String taskid,
                     @RequestParam("mobilenum") String mobilenum);
}