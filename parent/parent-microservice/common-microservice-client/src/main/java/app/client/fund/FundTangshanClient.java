package app.client.fund;

import com.crawler.housingfund.json.MessageLoginForHousing;
import com.crawler.housingfund.json.TaskHousingfund;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("HOUSINGFUND-TANGSHAN")
public interface FundTangshanClient {

    @PostMapping(value="/housing/tangshan/login")
    public TaskHousingfund login(@RequestBody MessageLoginForHousing messageLoginForHousing);

    @PostMapping(value="/housing/tangshan/setphonecode")
    public TaskHousingfund setcode(@RequestBody MessageLoginForHousing messageLoginForHousing);

    @PostMapping(value="/housing/tangshan/crawler")
    public TaskHousingfund crawler(@RequestBody MessageLoginForHousing messageLoginForHousing);
}
