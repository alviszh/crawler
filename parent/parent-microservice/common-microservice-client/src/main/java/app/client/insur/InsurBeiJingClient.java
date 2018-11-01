package app.client.insur;

import com.crawler.insurance.json.TaskInsurance;
import com.crawler.insurance.json.InsuranceRequestParameters;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name ="insurance-BeiJing",configuration = InsurClientConfig.class)
public interface InsurBeiJingClient {

    @PostMapping(value="/insurance/beijing/login")
    public TaskInsurance login(@RequestBody InsuranceRequestParameters insuranceRequestParameters);

    @PostMapping(value="/insurance/beijing/getdata")
    public TaskInsurance crawler(@RequestBody InsuranceRequestParameters insuranceRequestParameters);

    @PostMapping(value="/insurance/beijing/sendsms")
    public TaskInsurance sendsms(@RequestBody InsuranceRequestParameters insuranceRequestParameters);

    @GetMapping(value="/insurance/hystrix")
    public String hystrix();
}
