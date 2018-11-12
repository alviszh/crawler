package app.client.aws;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.crawler.aws.json.HttpProxyBean;

//@FeignClient("api-aws")
@FeignClient(name = "awsapi", url = "https://apitxboss.txtechnologies.com")
public interface AwsApiClient {

	//获取代理IP、端口
//	@GetMapping(value = "/aws/api/proxy/get")
	@RequestMapping(value = "/api-service/proxy/get",method = RequestMethod.GET)
	public HttpProxyBean getProxy() ;

}
