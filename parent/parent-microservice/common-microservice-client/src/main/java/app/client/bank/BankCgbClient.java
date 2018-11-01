package app.client.bank;

import com.crawler.bank.json.TaskBank;
import com.crawler.bank.json.BankJsonBean;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name ="bank-cgbchina",configuration = BankClientConfig.class)
public interface BankCgbClient {

    @PostMapping(value="/bank/cgbchina/loginAgent")
    public TaskBank loginD(@RequestBody BankJsonBean bankJsonBean);

    @PostMapping(value="/bank/cgbchina/credit/loginAgent")
    public TaskBank loginC(@RequestBody BankJsonBean bankJsonBean);

}
