package app.client.fund;

import com.crawler.housingfund.json.MessageLoginForHousing;
import com.crawler.housingfund.json.TaskHousingfund;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("HOUSINGFUND-CHANGZHI")
public interface FundChangzhiClient {
    @PostMapping(value="/housing/changzhi/login")
    public TaskHousingfund login(@RequestBody MessageLoginForHousing messageLoginForHousing);

}
