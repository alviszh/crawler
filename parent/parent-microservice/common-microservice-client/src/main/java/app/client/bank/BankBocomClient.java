package app.client.bank;

import com.crawler.bank.json.TaskBank;
import com.crawler.bank.json.BankJsonBean;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name ="BANK-BOCOM-DEBITCARD",configuration = BankClientConfig.class)
public interface BankBocomClient {

    @PostMapping(value="/bank/bocom/debitcard/loginAgent")
    public TaskBank loginD(@RequestBody BankJsonBean bankJsonBean);

    @PostMapping(value="/bank/bocom/debitcard/sendsms")
    public TaskBank sendSmsCodeD(@RequestBody BankJsonBean bankJsonBean);

}
