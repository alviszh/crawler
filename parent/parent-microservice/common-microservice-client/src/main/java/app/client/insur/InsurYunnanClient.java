package app.client.insur;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.crawler.insurance.json.TaskInsurance;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name ="Insurance-sz-YunNan",configuration = InsurClientConfig.class)
public interface InsurYunnanClient {

    @PostMapping(value="/insurance/yunnan/login")
    public TaskInsurance login(@RequestBody InsuranceRequestParameters parameter);

    @PostMapping(value="/insurance/yunnan/crawler")
    public TaskInsurance crawler(@RequestBody InsuranceRequestParameters parameter);
}
