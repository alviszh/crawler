package app.client.bank;

import com.crawler.bank.json.BankJsonBean;
import com.crawler.bank.json.TaskBank;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name ="BANK-SPDB-CREDITCARD",configuration = BankClientConfig.class)
public interface BankSpdbCreditClient {

    @PostMapping(value="/bank/spdbchina/creditcard/login")
    public TaskBank loginC(@RequestBody BankJsonBean bankJsonBean);

    @PostMapping(value="/bank/spdbchina/creditcard/sendSms")
    public TaskBank sendSmsCodeC(@RequestBody BankJsonBean bankJsonBean);

    @PostMapping(value="/bank/spdbchina/creditcard/verifySms")
    public TaskBank smsverfiyC(@RequestBody BankJsonBean bankJsonBean);

    @PostMapping(value="/bank/spdbchina/creditcard/getAllData")
    public TaskBank crawlerC(@RequestBody BankJsonBean bankJsonBean);

}
