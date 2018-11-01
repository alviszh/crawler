package app.client.insur;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.crawler.insurance.json.TaskInsurance;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name ="Insurance-Taian",configuration = InsurClientConfig.class)
public interface InsurTaianClient {

    @PostMapping(value="/insurance/taian/login")
    public TaskInsurance login(@RequestBody InsuranceRequestParameters parameter);
}
