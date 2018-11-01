package app.client.bank;

import com.crawler.bank.json.TaskBank;
import com.crawler.bank.json.BankJsonBean;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name ="bank-cmbchina-creditcard",configuration = BankClientConfig.class)
public interface BankCmbCreditClient {
    @PostMapping(value="/bank/cmbchina/creditcard/loginAgent")
    public TaskBank loginC(@RequestBody BankJsonBean bankJsonBean);

    @PostMapping(value="/bank/cmbchina/creditcard/crawlerAgent")
    public TaskBank crawlerC(@RequestBody BankJsonBean bankJsonBean);

    @PostMapping(value="/bank/cmbchina/creditcard/sendSmsCodeAgent")
    public TaskBank sendSmsCodeC(@RequestBody BankJsonBean bankJsonBean);

    @PostMapping(value="/bank/cmbchina/creditcard/smsverfiyAgent")
    public TaskBank  smsverfiyC(@RequestBody BankJsonBean bankJsonBean);
}
