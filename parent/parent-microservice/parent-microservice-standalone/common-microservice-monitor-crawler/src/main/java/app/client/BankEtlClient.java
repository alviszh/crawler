/**
 * 
 */
package app.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import com.crawler.monitor.json.tasker.MonitorBankTaskerBean;

/**
 * @author sln
 * @Description: 
 */
@FeignClient("BANK-ETL")    //微服务的名字
public interface BankEtlClient {
    @GetMapping(path = "/etl/bank/onedaybank")
	public List<MonitorBankTaskerBean> oneDayBank();
}
