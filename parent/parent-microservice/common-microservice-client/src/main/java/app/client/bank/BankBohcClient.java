package app.client.bank;

import com.crawler.bank.json.TaskBank;
import com.crawler.bank.json.BankJsonBean;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name ="bank-bohcchina",configuration = BankClientConfig.class)
public interface BankBohcClient {

    @PostMapping(value="/bank/bohcchina/loginAgent")
    public TaskBank loginD(@RequestBody BankJsonBean bankJsonBean);

    @PostMapping(value="/bank/bohcchina/verfiySMSAgent")
    public TaskBank firstV(@RequestBody BankJsonBean bankJsonBean);

}
