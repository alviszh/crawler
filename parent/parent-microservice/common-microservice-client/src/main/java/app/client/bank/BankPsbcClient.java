package app.client.bank;

import com.crawler.bank.json.TaskBank;
import com.crawler.bank.json.BankJsonBean;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name ="BANK-PSBCCHINA-DEBITCARD",configuration = BankClientConfig.class)
public interface BankPsbcClient {

    @PostMapping(value="/bank/psbcchina/debitcard/loginAgent")
    public TaskBank loginD(@RequestBody BankJsonBean bankJsonBean);

    @PostMapping(value="/bank/psbcchina/debitcard/crawlerAgent")
    public TaskBank crawlerD(@RequestBody BankJsonBean bankJsonBean);

}
