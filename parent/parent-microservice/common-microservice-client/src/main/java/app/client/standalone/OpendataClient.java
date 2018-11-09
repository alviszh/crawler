package app.client.standalone;

import com.crawler.opendata.json.developer.AppProductList;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "opendata", url = "http://10.167.202.216:4322")
public interface OpendataClient {


    @RequestMapping(value = "/opendata/api/developer/app/findAppProductList", method = RequestMethod.GET)
    public AppProductList findAppProductList(@RequestParam(name = "prodClientId") String prodClientId ,
                                              @RequestParam(name = "flag") String flag ,
                                              @RequestParam(name = "appmode") String appmode);
}
