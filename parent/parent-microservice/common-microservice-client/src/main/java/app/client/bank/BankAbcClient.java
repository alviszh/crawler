package app.client.bank;

import com.crawler.bank.json.TaskBank;
import com.crawler.bank.json.BankJsonBean;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name ="bank-abcchina",configuration = BankClientConfig.class)
public interface BankAbcClient {
    @PostMapping(value="/bank/abcchina/loginAgent")
    public TaskBank loginD(@RequestBody BankJsonBean bankJsonBean);

    @PostMapping(value="/bank/abcchina/verfiySMSAgent")
    public TaskBank sendSmsCodeD(@RequestBody BankJsonBean bankJsonBean);

    @PostMapping(value="/bank/abcchina/credit/loginAgent")
    public TaskBank loginC(@RequestBody BankJsonBean bankJsonBean);

    @PostMapping(value="/bank/abcchina/credit/verfiySMSAgent")
    public TaskBank sendSmsCodeC(@RequestBody BankJsonBean bankJsonBean);

}
