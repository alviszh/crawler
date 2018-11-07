package app.client.standalone;

import com.crawler.opendata.json.developer.AppProductList;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "opendata")
public interface OpendataClient {


    @RequestMapping(value = "/opendata/developer/app/appproduct", method = RequestMethod.GET)
    public AppProductList queryAppProductList(@RequestParam(name = "prodClientId") String prodClientId ,
                                              @RequestParam(name = "flag") String flag ,
                                              @RequestParam(name = "appmode") String appmode);
}
