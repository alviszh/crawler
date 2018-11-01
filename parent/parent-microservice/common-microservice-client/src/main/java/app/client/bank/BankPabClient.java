package app.client.bank;

import com.crawler.bank.json.TaskBank;
import com.crawler.bank.json.BankJsonBean;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name ="bank-pabchina",configuration = BankClientConfig.class)
public interface BankPabClient {

    @PostMapping(value="/bank/pabchina/loginAgent")
    public TaskBank loginD(@RequestBody BankJsonBean bankJsonBean);

    @PostMapping(value="/bank/pabchina/credit/loginAgent")
    public TaskBank loginC(@RequestBody BankJsonBean bankJsonBean);
}
