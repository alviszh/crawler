/**
 * 
 */
package app.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import com.crawler.monitor.json.tasker.MonitorHousingTaskerBean;

/**
 * @author sln
 * @Description: 
 */
//@FeignClient("HOUSINGFUND-ETL")    //微服务的名字
@FeignClient(name = "proxy", url = "${proxy}")
public interface HousingEtlClient {
    @GetMapping(path = "/etl/housing/onedayhousing")
	public List<MonitorHousingTaskerBean> oneDayHousing();
}
