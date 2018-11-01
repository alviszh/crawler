package app.client.fund;

import com.crawler.housingfund.json.TaskHousingfund;
import com.crawler.housingfund.json.MessageLoginForHousing;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("HousingFund-WenZhou")
public interface FundWenzhouClient {
    @PostMapping(value="/housing/wenzhou/getphonecode")
    public TaskHousingfund getcode(@RequestBody MessageLoginForHousing messageLoginForHousing);

    @PostMapping(value="/housing/wenzhou/setphonecode")
    public TaskHousingfund setcode(@RequestBody MessageLoginForHousing messageLoginForHousing);

    @PostMapping(value="/housing/wenzhou/crawler")
    public TaskHousingfund crawler(@RequestBody MessageLoginForHousing messageLoginForHousing);
}
