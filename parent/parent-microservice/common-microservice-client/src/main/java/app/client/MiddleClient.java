package app.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.crawler.bank.json.BrowserClusterBean;
import com.crawler.bank.json.IdleInstance;

@FeignClient(name ="middle",configuration = MiddleClientConfig.class)
public interface MiddleClient {
	
	// 客户端开始
	//@PostMapping(value = "/middle/clientFirst")
	//public ClientBean clientFirst(@RequestBody ClientBean clientBean);

	// 客户端结束
	//@PostMapping(value = "/middle/clientEnd")
	//public ClientBean clientEnd(@RequestBody ClientBean clientBean);
	
	
	//获取一个限制的实例 带上指定间隔时间
	@GetMapping(value = "/middle/getIdleInstance") 
	public IdleInstance getIdleInstance(@RequestParam("appName") String appName,@RequestParam("requestPath") String requestPath,@RequestParam("taskid") String taskid,@RequestParam("intervalTime") Long intervalTime) ;

	//获取一个限制的实例
	@GetMapping(value = "/middle/getIdleInstance") 
	public IdleInstance getIdleInstance(@RequestParam("appName") String appName,@RequestParam("requestPath") String requestPath,@RequestParam("taskid") String taskid) ;
	
	//释放一个使用完毕的实例
	@GetMapping(value = "/middle/releaseInstance")
	public IdleInstance releaseInstance(@RequestParam("appName") String appName,@RequestParam("instanceIpAddr") String instanceIpAddr) ;
	
	//通过Seleunim的 WindowsHandler id 获取该实例上的webdriver 对象继续使用（常用语短信验证码环节）
	@GetMapping(value = "/getInstanceByHandler")
	public IdleInstance getInstanceByHandler(String handler);
	
	//初始化一个实例
	@GetMapping(value = "/middle/init")
	public List<BrowserClusterBean> init(@RequestParam("appName") String appName);
	
}
