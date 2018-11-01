package app.client.insur;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.crawler.insurance.json.TaskInsurance;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name ="Insurance-Xiaogan",configuration = InsurClientConfig.class)
public interface InsurXiaoganClient {

    @PostMapping(value="/insurance/xiaogan/login")
    public TaskInsurance login(@RequestBody InsuranceRequestParameters parameter);

    @PostMapping(value="/insurance/xiaogan/crawler")
    public TaskInsurance crawler(@RequestBody InsuranceRequestParameters parameter);
}
