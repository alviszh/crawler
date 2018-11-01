package app.client.bank;

import com.crawler.bank.json.TaskBank;
import com.crawler.bank.json.BankJsonBean;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name ="bank-ccbchina",configuration = BankClientConfig.class)
public interface BankCcbClient {

    @PostMapping(value="/bank/ccbchina/debitcard/loginAgent")
    public TaskBank loginD(@RequestBody BankJsonBean bankJsonBean);

    @PostMapping(value="/bank/ccbchina/debitcard/sendsms")
    public TaskBank sendSmsCodeD(@RequestBody BankJsonBean bankJsonBean);

    @PostMapping(value="/bank/ccbchina/creditcard/loginAgent")
    public TaskBank loginC(@RequestBody BankJsonBean bankJsonBean);

}
