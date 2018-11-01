package app.client.fund;

import com.crawler.housingfund.json.TaskHousingfund;
import com.crawler.housingfund.json.MessageLoginForHousing;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("HousingFund-sz-Anhui")
public interface FundHefeiClient {

    @PostMapping(value="/housing/SZAnhui/login")
    public TaskHousingfund login(@RequestBody MessageLoginForHousing messageLoginForHousing);

    @PostMapping(value="/housing/SZAnhui/crawler")
    public TaskHousingfund crawler(@RequestBody MessageLoginForHousing messageLoginForHousing);
}
