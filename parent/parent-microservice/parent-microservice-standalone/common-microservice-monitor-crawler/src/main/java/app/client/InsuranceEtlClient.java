/**
 * 
 */
package app.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import com.crawler.monitor.json.tasker.MonitorInsuranceTaskerBean;

/**
 * @author sln
 * @Description: 
 */
//@FeignClient("INSURANCE-ETL")    //微服务的名字
@FeignClient(name = "h5-proxy", url = "${task-insurance-proxy}")
public interface InsuranceEtlClient {
    @GetMapping(path = "/etl/insurance/onedayinsurance")
	public List<MonitorInsuranceTaskerBean> oneDayInsurance();
}
