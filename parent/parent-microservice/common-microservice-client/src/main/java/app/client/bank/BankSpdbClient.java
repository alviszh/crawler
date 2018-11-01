package app.client.bank;

import com.crawler.bank.json.TaskBank;
import com.crawler.bank.json.BankJsonBean;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name ="bank-spdb",configuration = BankClientConfig.class)
public interface BankSpdbClient {

    @PostMapping(value="/bank/spdb/debitcard/loginAgent")
    public TaskBank loginD(@RequestBody BankJsonBean bankJsonBean);


}
