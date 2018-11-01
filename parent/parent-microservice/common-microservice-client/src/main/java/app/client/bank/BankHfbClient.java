package app.client.bank;

import com.crawler.bank.json.TaskBank;
import com.crawler.bank.json.BankJsonBean;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name ="bank-hfbchina-debitcard",configuration = BankClientConfig.class)
public interface BankHfbClient {

    @PostMapping(value="/bank/hfbchina/debitcard/loginAgent")
    public TaskBank loginD(@RequestBody BankJsonBean bankJsonBean);

    @PostMapping(value="/bank/hfbchina/debitcard/crawlerAgent")
    public TaskBank crawlerD(@RequestBody BankJsonBean bankJsonBean);

}
