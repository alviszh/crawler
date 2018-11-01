package app.client.fund;

import com.crawler.housingfund.json.TaskHousingfund;
import com.crawler.housingfund.json.MessageLoginForHousing;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
//@FeignClient(name = "HousingFund-TianJin", url = "http://10.167.110.36:9000")
@FeignClient("HousingFund-TianJin-local")
public interface FundTianjinClient {
    @PostMapping(value="/housing/tianjin/login")
    public TaskHousingfund login(@RequestBody MessageLoginForHousing messageLoginForHousing);


    @PostMapping(value="/housing/tianjin/crawler")
    public TaskHousingfund crawler(@RequestBody MessageLoginForHousing messageLoginForHousing);
}
