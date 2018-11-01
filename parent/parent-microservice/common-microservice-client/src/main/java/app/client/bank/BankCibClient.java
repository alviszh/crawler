package app.client.bank;

import com.crawler.bank.json.TaskBank;
import com.crawler.bank.json.BankJsonBean;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name ="bank-cibchina",configuration = BankClientConfig.class)
public interface BankCibClient {

    @PostMapping(value="/bank/cibchina/debitcard/loginAgent")
    public TaskBank loginD(@RequestBody BankJsonBean bankJsonBean);

    @PostMapping(value="/bank/cibchina/debitcard/sendSmsAgent")
    public TaskBank sendSmsCodeD(@RequestBody BankJsonBean bankJsonBean);

    @PostMapping(value="/bank/cibchina/debitcard/verifySmsAgent")
    public TaskBank smsverfiyD(@RequestBody BankJsonBean bankJsonBean);

    @PostMapping(value="/bank/cibchina/debitcard/getAllDataAgent")
    public TaskBank crawlerD(@RequestBody BankJsonBean bankJsonBean);

    @PostMapping(value="/bank/cibchina/creditcard/loginAgent")
    public TaskBank loginC(@RequestBody BankJsonBean bankJsonBean);

    @PostMapping(value="/bank/cibchina/creditcard/sendSmsAgent")
    public TaskBank sendSmsCodeC(@RequestBody BankJsonBean bankJsonBean);

    @PostMapping(value="/bank/cibchina/creditcard/verifySmsAgent")
    public TaskBank smsverfiyC(@RequestBody BankJsonBean bankJsonBean);

    @PostMapping(value="/bank/cibchina/creditcard/getAllDataAgent")
    public TaskBank crawlerC(@RequestBody BankJsonBean bankJsonBean);

}
