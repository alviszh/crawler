package app.client.fund;

import com.crawler.housingfund.json.MessageLoginForHousing;
import com.crawler.housingfund.json.TaskHousingfund;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("HOUSINGFUND-YINCHUAN")
public interface FundYinchuanClient {

    @PostMapping(value="/housing/yinchuan/login")
    public TaskHousingfund login(@RequestBody MessageLoginForHousing messageLoginForHousing);

    @PostMapping(value="/housing/yinchuan/loginCombo")
    public TaskHousingfund setcode(@RequestBody MessageLoginForHousing messageLoginForHousing);

    @PostMapping(value="/housing/yinchuan/crawler")
    public TaskHousingfund crawler(@RequestBody MessageLoginForHousing messageLoginForHousing);
}
