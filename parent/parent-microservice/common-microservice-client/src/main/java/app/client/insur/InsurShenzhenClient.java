package app.client.insur;

import com.crawler.insurance.json.TaskInsurance;
import com.crawler.insurance.json.InsuranceRequestParameters;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name ="Insurance-Shenzhen",configuration = InsurClientConfig.class)
public interface InsurShenzhenClient {

    @PostMapping(value="/insurance/shenzhen/login")
    public TaskInsurance login(@RequestBody InsuranceRequestParameters parameter);

    @PostMapping(value="/insurance/shenzhen/crawler")
    public TaskInsurance crawler(@RequestBody InsuranceRequestParameters parameter);
}
