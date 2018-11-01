package app.service.shixin.find;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import com.microservice.dao.entity.crawler.honesty.shixin.HonestyTask;

import app.bean.error.ErrorException;
import app.commontracerlog.TracerLog;
import app.service.shixin.ExecutorshixinCodeService;
import app.service.shixin.ExecutorshixinBeanCrawlerService;

/**
 * 
 * 项目名称：common-microservice-executor 类名称：ExecutorGetHtmlService 类描述： 创建人：hyx
 * 创建时间：2018年7月12日 上午11:08:38
 * 
 * @version
 */
@Component
public class ExecutorShiXinCrawlerService {

	@Autowired
	private ExecutorshixinBeanCrawlerService executorshixinBeanCrawlerService;

	@Autowired
	private ExecutorshixinCodeService executorshixinCodeService;

	@Autowired
	private TracerLog tracerLog;

	private String code = null;

	private long start, end;

	@Retryable(value = ErrorException.class, maxAttempts = 10)
	public void crawler(List<Integer> shixinid_list, HonestyTask honestyTask,String captchaId) {
		tracerLog.System("start shixinid", shixinid_list.toString() + "");


		long startTime = System.currentTimeMillis();
		if (((end - start) / 1000) > 20) {
			start = 0;
			end = 0;
			code = executorshixinCodeService.getCode(honestyTask, captchaId);

			tracerLog.System("验证码过期", code + "");
		}
		if (start == 0) {
			start = System.currentTimeMillis();
			try {
			
				code = executorshixinCodeService.getCode(honestyTask, captchaId);
			} catch (Exception e) {
				e.printStackTrace();
				start = 0;
				end = 0;
				tracerLog.System("code 验证码失败", e.getMessage() + "");
			}
		}

		tracerLog.System("start code", code + "");

		List<Future<String>> future_list = new ArrayList<>();
		start = System.currentTimeMillis();

		for (Integer shixinid : shixinid_list) {

			Future<String> future = executorshixinBeanCrawlerService.getShiXinBreakPromise(captchaId, shixinid, code,
					honestyTask);
			future_list.add(future);
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			end = System.currentTimeMillis();

			if (((end - start) / 1000) > 20) {
				start = System.currentTimeMillis();
				end = 0;
				tracerLog.System("验证码过期", code + "");
				code = executorshixinCodeService.getCode(honestyTask, captchaId);
				tracerLog.System("重新获取验证码", code + "");
			}
		}
		boolean isdone = true;
		tracerLog.System("future_list start", future_list.size() + "");
		long startTime222 = System.currentTimeMillis();
		while (isdone) {
			for (Future<String> future : future_list) {

				if (future.isDone()) { // 判断是否执行完毕
					future_list.remove(future);
					tracerLog.System("future_list", future_list.size() + "");

					break;
				}

			}
			if (future_list.size() <= 0) {
				isdone = false;
			}

		}
		end = System.currentTimeMillis();

		if (((end - start) / 1000) > 20) {
			start = 0;
			end = 0;
			tracerLog.System("验证码过期", code + "");
		}

		long endTime = System.currentTimeMillis();
		tracerLog.System("future_list.size()---" + future_list.size() + "当前共计程序耗时ms：", (endTime - startTime) + "ms");
		tracerLog.System("future_list.size()---" + future_list.size() + "当前共计程序耗时s：", (endTime - startTime222) + "ms");

	}

}
