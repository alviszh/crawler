package app.client.ecom;

import com.crawler.e_commerce.json.E_CommerceTask;
import com.crawler.e_commerce.json.E_CommerceJsonBean;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
@FeignClient(name ="ECOMMERCE-TAOBAO",configuration = TaobaoClientConfig.class)
public interface TbClient {
//    @PostMapping(value="/e_commerce/taobao/loginAgent")
//    public E_CommerceTask getTaobaoQRcode(@RequestBody E_CommerceJsonBean e_CommerceJsonBean);

    @PostMapping(value="/e_commerce/taobao/alipay/loginAgent")
    public E_CommerceTask login(@RequestBody E_CommerceJsonBean e_CommerceJsonBean);

    @PostMapping(value="/e_commerce/taobao/base64ImageAgent")
    public E_CommerceTask base64Tb(@RequestBody E_CommerceJsonBean e_CommerceJsonBean);

    @PostMapping(value="/e_commerce/taobao/alipay/base64ImageAgent")
    public E_CommerceTask base64Ap(@RequestBody E_CommerceJsonBean e_CommerceJsonBean);

    @PostMapping(value="/e_commerce/taobao/checkQRcodeAgent")
    public E_CommerceTask checkQRcodeTb(@RequestBody E_CommerceJsonBean e_CommerceJsonBean);

    @PostMapping(value="/e_commerce/taobao/alipay/checkQRcodeAgent")
    public E_CommerceTask checkQRcodeAp(@RequestBody E_CommerceJsonBean e_CommerceJsonBean);

    @PostMapping(value="/e_commerce/taobao/verfiySMSAgent")
    public E_CommerceTask verfiySMS(@RequestBody E_CommerceJsonBean e_CommerceJsonBean);

    @PostMapping(value="/e_commerce/taobao/crawlerAgent")
    public E_CommerceTask crawler(@RequestBody E_CommerceJsonBean e_CommerceJsonBean);

    @PostMapping(value="/e_commerce/taobao/quit")
    public E_CommerceTask quit(@RequestBody E_CommerceJsonBean e_CommerceJsonBean);

}
