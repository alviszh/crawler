package app.client.ecom;

import com.crawler.e_commerce.json.E_CommerceTask;
import com.crawler.e_commerce.json.E_CommerceJsonBean;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@FeignClient(name ="ECOMMERCE-JINGDONG",configuration = JdClientConfig.class)
public interface JdClient {
    @PostMapping(value="/e_commerce/jd/loginAgent")
    public E_CommerceTask login(@RequestBody E_CommerceJsonBean e_CommerceJsonBean);

    @PostMapping(value="/e_commerce/jd/sendSmsCodeAgent")
    public E_CommerceTask sendSmsCode(@RequestBody E_CommerceJsonBean e_CommerceJsonBean);

    @PostMapping(value="/e_commerce/jd/checkQRcodeAgent")
    public E_CommerceTask checkQRcode(@RequestBody E_CommerceJsonBean e_CommerceJsonBean);

    @PostMapping(value="/e_commerce/jd/base64ImageAgent")
    public E_CommerceTask base64(@RequestBody E_CommerceJsonBean e_CommerceJsonBean);

    @PostMapping(value="/e_commerce/jd/quit")
    public E_CommerceTask quit(@RequestBody E_CommerceJsonBean e_CommerceJsonBean);
}
