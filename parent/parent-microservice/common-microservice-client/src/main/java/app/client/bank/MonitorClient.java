package app.client.bank;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name ="MONITOR",configuration = BankClientConfig.class)
public interface MonitorClient {
    @PostMapping(value="/monitor/bankusable")
    public String monitor();
}
