package app.client.insur;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.crawler.insurance.json.TaskInsurance;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name ="Insurance-Maoming",configuration = InsurClientConfig.class)
public interface InsurMaomingClient {
    @PostMapping(value="/insurance/Maoming/login")
    public TaskInsurance login(@RequestBody InsuranceRequestParameters insuranceRequestParameters);

    @PostMapping(value="/insurance/Maoming/crawler")
    public TaskInsurance crawler(@RequestBody InsuranceRequestParameters insuranceRequestParameters);

}
