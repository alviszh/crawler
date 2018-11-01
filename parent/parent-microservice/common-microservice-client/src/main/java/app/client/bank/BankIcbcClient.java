package app.client.bank;

import com.crawler.bank.json.TaskBank;
import com.crawler.bank.json.BankJsonBean;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name ="bank-icbcchina",configuration = BankClientConfig.class)
public interface BankIcbcClient {

    @PostMapping(value="/bank/icbcchina/debitcard/loginAgent")
    public TaskBank loginD(@RequestBody BankJsonBean bankJsonBean);

    @PostMapping(value="/bank/icbcchina/debitcard/setSMSCodeAgent")
    public TaskBank sendSmsCodeD(@RequestBody BankJsonBean bankJsonBean);

    @PostMapping(value="/bank/icbcchina/creditcard/loginAgent")
    public TaskBank loginC(@RequestBody BankJsonBean bankJsonBean);

    @PostMapping(value="/bank/icbcchina/creditcard/setSMSCodeAgent")
    public TaskBank sendSmsCodeC(@RequestBody BankJsonBean bankJsonBean);

}
