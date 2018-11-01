package app.client.fund;

import com.crawler.housingfund.json.MessageLoginForHousing;
import com.crawler.housingfund.json.TaskHousingfund;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("HOUSINGFUND-YICHUN")
public interface FundYichunClient {

    @PostMapping(value="/housing/yichun/login")
    public TaskHousingfund login(@RequestBody MessageLoginForHousing messageLoginForHousing);

 }
