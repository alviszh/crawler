package app.client.insur;

import com.crawler.insurance.json.TaskInsurance;
import com.crawler.insurance.json.InsuranceRequestParameters;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name ="Insurance-Yantai",configuration = InsurClientConfig.class)
public interface InsurYantaiClient {

    @PostMapping(value = "/insurance/yantai/login")
    public TaskInsurance login(@RequestBody InsuranceRequestParameters parameter);

    @PostMapping(value = "/insurance/yantai/crawler")
    public TaskInsurance crawler(@RequestBody InsuranceRequestParameters parameter);

    @PostMapping(value = "/insurance/yantai/verificationCode")
    public List<InsuranceRequestParameters> verificationCode(@RequestBody InsuranceRequestParameters parameter);
}
