package app.client.fund;

import com.crawler.housingfund.json.TaskHousingfund;
import com.crawler.housingfund.json.MessageLoginForHousing;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("HousingFund-NanChang")
public interface FundNanchangClient {
    @PostMapping(value="/housing/nanchang/crawler")
    public TaskHousingfund crawler(@RequestBody MessageLoginForHousing messageLoginForHousing);
}
