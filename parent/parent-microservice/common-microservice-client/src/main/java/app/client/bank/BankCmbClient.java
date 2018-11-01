package app.client.bank;

import com.crawler.bank.json.TaskBank;
import com.crawler.bank.json.BankJsonBean;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name ="bank-cmbchina-debitcard",configuration = BankClientConfig.class)
public interface BankCmbClient {
    @PostMapping(value="/bank/cmbchina/debitcard/loginAgent")
    public TaskBank loginD(@RequestBody BankJsonBean bankJsonBean);

    @PostMapping(value="/bank/cmbchina/debitcard/crawlerAgent")
    public TaskBank crawlerD(@RequestBody BankJsonBean bankJsonBean);

    @PostMapping(value="/bank/cmbchina/debitcard/sendSmsCodeAgent")
    public TaskBank sendSmsCodeD(@RequestBody BankJsonBean bankJsonBean);

    @PostMapping(value="/bank/cmbchina/debitcard/smsverfiyAgent")
    public TaskBank  smsverfiyD(@RequestBody BankJsonBean bankJsonBean);
}
