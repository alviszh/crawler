package app.client.bank;

import com.crawler.bank.json.TaskBank;
import com.crawler.bank.json.BankJsonBean;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name ="bank-cebchina",configuration = BankClientConfig.class)
public interface BankCebClient {

    @PostMapping(value="/bank/cebchina/debitcard/loginAgent")
    public TaskBank loginD(@RequestBody BankJsonBean bankJsonBean);

    @PostMapping(value="/bank/cebchina/debitcard/sendSmsCodeAgent")
    public TaskBank sendSmsCodeD(@RequestBody BankJsonBean bankJsonBean);

}
