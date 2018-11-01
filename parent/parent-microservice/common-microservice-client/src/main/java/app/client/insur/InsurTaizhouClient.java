package app.client.insur;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.crawler.insurance.json.TaskInsurance;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name ="Insurance-Taizhou",configuration = InsurClientConfig.class)
public interface InsurTaizhouClient {

    @PostMapping(value="/insurance/taizhou/login")
    public TaskInsurance login(@RequestBody InsuranceRequestParameters parameter);

    @PostMapping(value="/insurance/taizhou/crawler")
    public TaskInsurance crawler(@RequestBody InsuranceRequestParameters parameter);
}
