package app.client.insur;

import com.crawler.insurance.json.TaskInsurance;
import com.crawler.insurance.json.InsuranceRequestParameters;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name ="Insurance-Tieling",configuration = InsurClientConfig.class)
public interface InsurTielingClient {

    @PostMapping(value="/insurance/tieling/login")
    public TaskInsurance login(@RequestBody InsuranceRequestParameters parameter);

    @PostMapping(value="/insurance/tieling/getAllData")
    public TaskInsurance crawler(@RequestBody InsuranceRequestParameters parameter);
}
