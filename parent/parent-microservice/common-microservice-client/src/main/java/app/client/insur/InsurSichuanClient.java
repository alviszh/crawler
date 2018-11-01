package app.client.insur;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.crawler.insurance.json.TaskInsurance;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name ="insurance-sz-sichuan",configuration = InsurClientConfig.class)
public interface InsurSichuanClient {
    @PostMapping(value = "/insurance/szsichuan")
    public TaskInsurance crawler(@RequestBody InsuranceRequestParameters insuranceRequestParameters);


}
