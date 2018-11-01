package app.client.fund;

import com.crawler.housingfund.json.MessageLoginForHousing;
import com.crawler.housingfund.json.TaskHousingfund;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("HOUSINGFUND-XUCHANG")
public interface FundXuchangClient {
    @PostMapping(value="/housing/xuchang/login")
    public TaskHousingfund login(@RequestBody MessageLoginForHousing messageLoginForHousing);

    @PostMapping(value="/housing/xuchang/crawler")
    public TaskHousingfund crawler(@RequestBody MessageLoginForHousing messageLoginForHousing);
}
