package app.client.insur;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.crawler.insurance.json.TaskInsurance;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name ="INSURANCE-LIAOCHENG",configuration = InsurClientConfig.class)
public interface InsurLiaochengClient {

    @PostMapping(value="/insurance/liaocheng/login")
    public TaskInsurance login(@RequestBody InsuranceRequestParameters insuranceRequestParameters);

}
