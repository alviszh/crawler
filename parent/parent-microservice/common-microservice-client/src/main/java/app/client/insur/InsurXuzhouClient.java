package app.client.insur;

import com.crawler.insurance.json.TaskInsurance;
import com.crawler.insurance.json.InsuranceRequestParameters;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name ="Insurance-Xuzhou",configuration = InsurClientConfig.class)
public interface InsurXuzhouClient {

    @PostMapping(value="/insurance/xuzhou/login")
    public TaskInsurance login(@RequestBody InsuranceRequestParameters insuranceRequestParameters);

    @PostMapping(value="/insurance/xuzhou/getAllData")
    public TaskInsurance crawler(@RequestBody InsuranceRequestParameters insuranceRequestParameters);
}
