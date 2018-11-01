package app.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import app.entity.insuranceETL.WebData;


@FeignClient(name = "insuranceETL", url = "${api.insuranceETL.url}")
//@FeignClient(name = "insuranceETL", url = "http://10.167.35.19:8001")
public interface InsuranceETLClient {

    @GetMapping("/etl/insurance/detail")
    WebData insuranceInfo(@RequestParam("taskid") String taskid,@RequestParam("idnum") String idnum);
}
