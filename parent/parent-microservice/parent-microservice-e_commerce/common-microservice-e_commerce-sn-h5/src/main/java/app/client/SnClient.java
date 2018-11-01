package app.client;

import app.bean.E_CommerceTask;
import com.crawler.e_commerce.json.E_CommerceJsonBean;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name ="ECOMMERCE-SUNING",configuration = SnClientConfig.class)
public interface SnClient {
    @PostMapping(value="/ds/suning/loginAgent")
    public E_CommerceTask login(@RequestBody E_CommerceJsonBean e_CommerceJsonBean);

    @PostMapping(value="/ds/suning/sendSMS")
    public E_CommerceTask sendSMS(@RequestBody E_CommerceJsonBean e_CommerceJsonBean);
}
