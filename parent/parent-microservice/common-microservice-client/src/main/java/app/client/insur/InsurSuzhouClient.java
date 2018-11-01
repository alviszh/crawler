package app.client.insur;

import com.crawler.insurance.json.TaskInsurance;
import com.crawler.insurance.json.InsuranceRequestParameters;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name ="Insurance-Suzhou",configuration = InsurClientConfig.class)
public interface InsurSuzhouClient {

    @PostMapping(value="/insurance/suzhou")
    public TaskInsurance login(@RequestBody InsuranceRequestParameters parameter);

    @PostMapping(value="/insurance/suzhou/getAllInfo")
    public TaskInsurance crawler(@RequestBody InsuranceRequestParameters parameter);
}
