package app.client.aws;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.crawler.aws.json.HttpProxyBean;
import com.crawler.aws.json.HttpProxyRes;

//@FeignClient("api-aws")
@FeignClient(name = "awsapi", url = "https://apitxboss.txtechnologies.com")
public interface AwsApiClient {

	//获取代理IP、端口
//	@GetMapping(value = "/aws/api/proxy/get")
	@RequestMapping(value = "/api-service/proxy/get",method = RequestMethod.GET)
	public HttpProxyBean getProxy() ;


	//获取AWS的代理
	@RequestMapping(value = "/api-service/proxy/aws/get",method = RequestMethod.GET)
	public HttpProxyRes getApiProxy();

	/**   
	 * @Title: getResProxy   
	 * @Description:  获取代理ip ，不穿参数默认为1
	 * @param: @return      
	 * @return: HttpProxyRes      
	 * @throws   
	 */ 
	@RequestMapping(value = "/api-service/proxy/get",method = RequestMethod.GET)
	public HttpProxyRes getProxy(@RequestParam("num")int num) ;

}
