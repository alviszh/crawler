package app.client.fund;

import com.crawler.housingfund.json.MessageLoginForHousing;
import com.crawler.housingfund.json.TaskHousingfund;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("HousingFund-Guilin")
public interface FundGuilinClient {

    @PostMapping(value="/housing/guilin/login")
    public TaskHousingfund login(@RequestBody MessageLoginForHousing messageLoginForHousing);

 }
