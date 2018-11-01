package app.client.bank;

import com.crawler.bank.json.TaskBank;
import com.crawler.bank.json.BankJsonBean;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name ="bank-hxbchina",configuration = BankClientConfig.class)
public interface BankHxbClient {
    @PostMapping(value="/bank/hxbchina/debitcard/loginAgent")
    public TaskBank loginD(@RequestBody BankJsonBean bankJsonBean);

    @PostMapping(value="/bank/hxbchina/debitcard/crawlerAgent")
    public TaskBank crawlerD(@RequestBody BankJsonBean bankJsonBean);

    @PostMapping(value="/bank/hxbchina/creditcard/loginAgent")
    public TaskBank loginC(@RequestBody BankJsonBean bankJsonBean);

    @PostMapping(value="/bank/hxbchina/creditcard/crawlerAgent")
    public TaskBank crawlerC(@RequestBody BankJsonBean bankJsonBean);
}
