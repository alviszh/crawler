package app.client.bank;

import com.crawler.bank.json.TaskBank;
import com.crawler.bank.json.BankJsonBean;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

//@FeignClient(name ="bank-citicchina-creditcard",configuration = BankClientConfig.class)
@FeignClient(name ="bank-citicchina-creditcard",url = "http://10.167.35.16:85")
public interface BankCiticCreditClient {

    @PostMapping(value="/bank/citicchina/creditcard/loginHtmlunit")
    public TaskBank sendSmsCodeC(@RequestBody BankJsonBean bankJsonBean);

    @PostMapping(value="/bank/citicchina/creditcard/saveCode")
    public TaskBank loginC(@RequestBody BankJsonBean bankJsonBean);

    @PostMapping(value="/bank/citicchina/creditcard/crawler")
    public TaskBank crawlerC(@RequestBody BankJsonBean bankJsonBean);

}
