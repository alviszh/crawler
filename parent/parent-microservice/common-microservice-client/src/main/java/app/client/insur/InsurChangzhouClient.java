package app.client.insur;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.crawler.insurance.json.TaskInsurance;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name ="Insurance-ChangZhou",configuration = InsurClientConfig.class)
public interface InsurChangzhouClient {

    @PostMapping(value="/insurance/changzhou/crawler")
    public TaskInsurance login(@RequestBody InsuranceRequestParameters insuranceRequestParameters);
}
