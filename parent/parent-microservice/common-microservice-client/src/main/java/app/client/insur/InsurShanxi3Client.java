package app.client.insur;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.crawler.insurance.json.TaskInsurance;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name ="Insurance-ShanXi3",configuration = InsurClientConfig.class)
public interface InsurShanxi3Client {

    @PostMapping(value = "/insurance/szshanxi3")
    public TaskInsurance login(@RequestBody InsuranceRequestParameters insuranceRequestParameters);

}
