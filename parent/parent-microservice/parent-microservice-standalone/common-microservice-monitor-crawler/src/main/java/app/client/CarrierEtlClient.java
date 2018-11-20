/**
 * 
 */
package app.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import com.crawler.monitor.json.tasker.MonitorCarrierTempBean;

/**
 * @author sln
 * @Description: 
 */
//@FeignClient("MOBILE-ETL")    //微服务的名字
@FeignClient(name = "h5-proxy",configuration = ClientConfig.class, url = "${h5-proxy}")
public interface CarrierEtlClient {
    //如下调用运营商的定时任务执行结果
    @GetMapping(path = "/etl/carrier/onedaycarrier")
	public List<MonitorCarrierTempBean> oneDayCarrier();
	//获取10天的运行结果
    @GetMapping(path = "/etl/carrier/moredaycarrier")
	public List<MonitorCarrierTempBean> moreDayCarrier();
}
