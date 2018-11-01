package app.client.insur;

import com.crawler.insurance.json.TaskInsurance;
import com.crawler.insurance.json.InsuranceRequestParameters;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name ="Insurance-sz-JiLin",configuration = InsurClientConfig.class)
public interface InsurSzJilinClient {
    @PostMapping(value="/insurance/szjilin/login")
    public TaskInsurance login(@RequestBody InsuranceRequestParameters insuranceRequestParameters);

    @PostMapping(value="/insurance/szjilin//crawler")
    public TaskInsurance crawler(@RequestBody InsuranceRequestParameters insuranceRequestParameters);

}
