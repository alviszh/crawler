package app.client.bank;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 回调接口（汇诚）

@FeignClient(name = "bank", url = "${api.bank.result.url}")
public interface BankResultClient {


     * 发送征信报告
     * @param bankReport
     * @param key
     * @return

    @PostMapping("/as-adapter-ext/keji/pbccredit/report")
     String sendBankReport(@RequestBody String bankReport,
                           @RequestParam("key") String key);
}
        */